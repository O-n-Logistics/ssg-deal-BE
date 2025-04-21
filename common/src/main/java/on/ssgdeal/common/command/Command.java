package on.ssgdeal.common.command;

public interface Command<T> {

    /**
 * 명령을 실행하고 결과를 반환합니다.
 *
 * @return 실행 결과 객체
 */
T execute();

    /**
 * 실행된 명령의 효과를 취소하거나 되돌립니다.
 */
void undo();
}