package on.ssgdeal.common.command;

import java.util.ArrayDeque;
import java.util.Deque;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScopedCommandInvoker implements AutoCloseable {

    private final CommandInvoker commandInvoker;

    /**
     * ScopedCommandInvoker의 인스턴스를 생성하고 내부 명령 실행기를 초기화합니다.
     */
    public ScopedCommandInvoker() {
        this.commandInvoker = new CommandInvoker();
    }

    /**
     * 지정된 커맨드를 실행하고 결과를 반환합니다.
     *
     * 커맨드는 실행 후 내부 스택에 저장되어, 이후 undo 기능을 사용할 수 있습니다.
     *
     * @param command 실행할 커맨드
     * @return 커맨드 실행 결과
     */
    public <T> T executeCommand(Command<T> command) {
        return commandInvoker.executeCommand(command);
    }

    /**
     * 마지막으로 실행된 명령을 취소합니다.
     *
     * 실행된 명령이 없거나 취소 중 예외가 발생해도 예외는 전파되지 않습니다.
     */
    public void undoLastCommand() {
        commandInvoker.undoLastCommand();
    }

    /**
     * 실행된 모든 명령을 역순으로 취소합니다.
     *
     * 명령 기록 스택에 저장된 모든 명령의 undo 메서드를 호출하여 실행을 취소합니다. 각 undo 과정에서 발생하는 예외는 경고로 기록되며, 예외가 전파되지는 않습니다.
     */
    public void undoAllCommands() {
        commandInvoker.undoAllCommands();
    }

    /**
     * 현재 스레드의 커맨드 실행 이력을 모두 삭제합니다.
     *
     * 이 메서드는 실행 취소(undo) 기록을 초기화하여, 이후 undo 관련 메서드 호출 시 이전 커맨드가 존재하지 않도록 만듭니다.
     */
    public void clear() {
        commandInvoker.clear();
    }

    /**
     * ScopedCommandInvoker가 닫힐 때 명령 기록을 모두 삭제합니다.
     *
     * AutoCloseable 인터페이스의 close 메서드를 구현하여, 리소스 정리 시 명령 스택을 비웁니다.
     */
    @Override
    public void close() {
        clear();
    }

    private static class CommandInvoker {

        private final ThreadLocal<Deque<Command<?>>> commandHistoryHolder =
            ThreadLocal.withInitial(ArrayDeque::new);

        /**
         * CommandInvoker의 인스턴스 생성을 방지하는 private 생성자입니다.
         */
        private CommandInvoker() {
        }

        /**
         * 주어진 커맨드를 실행하고, 실행된 커맨드를 현재 스레드의 히스토리에 추가합니다.
         *
         * @param command 실행할 커맨드
         * @return 커맨드 실행 결과
         */
        public <T> T executeCommand(Command<T> command) {
            T result = command.execute();
            commandHistoryHolder.get().push(command);
            return result;
        }

        /**
         * 마지막으로 실행된 명령을 실행 취소합니다.
         *
         * 명령의 undo 과정에서 예외가 발생하면 경고 로그를 남기고 예외는 전파하지 않습니다.
         */
        public void undoLastCommand() {
            Deque<Command<?>> history = commandHistoryHolder.get();

            if (!history.isEmpty()) {
                Command<?> command = history.pop();
                try {
                    command.undo();
                } catch (Exception e) {
                    log.warn("Undo failed for command {}: {}", command.getClass().getSimpleName(),
                        e.getMessage(), e);
                }
            }
        }

        /**
         * 실행된 모든 명령을 스택에서 꺼내 역순으로 undo합니다.
         * <p>
         * 각 명령의 undo 중 예외가 발생해도 로그만 남기고 다음 명령의 undo를 계속 시도합니다.
         */
        public void undoAllCommands() {
            Deque<Command<?>> history = commandHistoryHolder.get();

            while (!history.isEmpty()) {
                Command<?> command = history.pop();
                try {
                    command.undo();
                } catch (Exception e) {
                    log.warn("Undo failed for command {}: {}", command.getClass().getSimpleName(),
                        e.getMessage(), e);
                }
            }
        }

        /**
         * 현재 스레드의 커맨드 실행 이력을 모두 삭제합니다.
         *
         * 이 메서드는 커맨드 언두(undo) 기록을 초기화하여, 이후 언두 작업이 불가능하도록 만듭니다.
         */
        public void clear() {
            commandHistoryHolder.remove();
        }
    }
}