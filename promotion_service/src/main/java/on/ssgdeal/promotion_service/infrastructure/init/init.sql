START TRANSACTION;

-- Promotion 더미 데이터
INSERT INTO promotion (
    id, title, preview, content, content_image_url, status,
    start_promotion_date, end_promotion_date,
    created_at, created_by, updated_at, updated_by,
    is_deleted
) VALUES
      (1, '봄맞이 세일', '지금 쇼핑하면 최대 50% 할인!', '봄 시즌 프로모션 내용입니다.', 'https://example.com/image1.jpg', 'IN_PROGRESS',
       CURRENT_TIMESTAMP - INTERVAL 1 DAY, CURRENT_TIMESTAMP + INTERVAL 10 DAY,
       CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

      (2, '여름 바캉스 특가', '휴가 준비는 지금부터!', '여름 프로모션 상세 설명입니다.', 'https://example.com/image2.jpg', 'IN_PROGRESS',
       CURRENT_TIMESTAMP + INTERVAL 5 DAY, CURRENT_TIMESTAMP + INTERVAL 15 DAY,
       CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

      (3, '가을 신상품 출시', '가을 트렌드를 만나보세요.', '가을 패션, 리빙 신상품 프로모션.', 'https://example.com/image3.jpg', 'PENDING',
       CURRENT_TIMESTAMP - INTERVAL 3 DAY, CURRENT_TIMESTAMP + INTERVAL 20 DAY,
       CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

      (4, '겨울 한정 기획전', '겨울 필수템 모음전!', '한파 대비 기획전입니다.', 'https://example.com/image4.jpg', 'FINISHED',
       CURRENT_TIMESTAMP - INTERVAL 30 DAY, CURRENT_TIMESTAMP - INTERVAL 1 DAY,
       CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

      (5, '신년 맞이 혜택', '2025년 새해 혜택 받고 시작하세요!', '새해 프로모션, 쿠폰, 포인트 지급.', 'https://example.com/image5.jpg', 'FINISHED',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL 7 DAY,
       CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false);

-- Company 더미 데이터 (promotion_id 순서 보장)
INSERT INTO company (
    id, name, logo_url, manager_id, promotion_id,
    created_at, created_by, updated_at, updated_by,
    is_deleted
) VALUES
-- promotion_id = 1
(1, '봄나들이 마켓', 'https://example.com/logo1.png', 1, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(2, '벚꽃몰', 'https://example.com/logo2.png', 2, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(3, '가드닝 스토어', 'https://example.com/logo3.png', 3, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(4, '스프링리빙', 'https://example.com/logo4.png', 4, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- promotion_id = 2
(5, '썸머스토어', 'https://example.com/logo5.png', 5, 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(6, '비치웨어샵', 'https://example.com/logo6.png', 6, 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(7, '서머가전몰', 'https://example.com/logo7.png', 7, 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(8, '바캉스팜', 'https://example.com/logo8.png', 8, 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- promotion_id = 3
(9, '가을옷장', 'https://example.com/logo9.png', 9, 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(10, '어텀홈', 'https://example.com/logo10.png', 10, 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(11, '브라운컬렉션', 'https://example.com/logo11.png', 11, 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(12, '코지샵', 'https://example.com/logo12.png', 12, 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- promotion_id = 4
(13, '윈터에디션', 'https://example.com/logo13.png', 13, 4, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(14, '눈꽃마켓', 'https://example.com/logo14.png', 14, 4, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(15, '한파대비몰', 'https://example.com/logo15.png', 15, 4, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(16, '따숩상점', 'https://example.com/logo16.png', 16, 4, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- promotion_id = 5
(17, '해돋이샵', 'https://example.com/logo17.png', 17, 5, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(18, '새해마켓', 'https://example.com/logo18.png', 18, 5, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(19, '2025키트몰', 'https://example.com/logo19.png', 19, 5, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(20, '혜택상점', 'https://example.com/logo20.png', 20, 5, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false);

-- Product 더미 데이터
INSERT INTO product (
    id, name, original_price, promotion_price,
    preview_url, content_image_url, content, company_id,
    created_at, created_by, updated_at, updated_by,
    is_deleted
) VALUES
-- Company 1 ~ 3 (promotion_id = 1)
(1, '벚꽃 에코백', 10000, 8000, 'https://example.com/preview1.jpg', 'https://example.com/content1.jpg', '벚꽃 시즌용 에코백입니다.', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(2, '봄꽃 파자마', 20000, 15000, 'https://example.com/preview2.jpg', 'https://example.com/content2.jpg', '봄밤용 편한 파자마.', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(3, '가든 장갑', 15000, 12000, 'https://example.com/preview3.jpg', 'https://example.com/content3.jpg', '원예 작업용 장갑입니다.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(4, '플로럴 워터병', 18000, 16000, 'https://example.com/preview4.jpg', 'https://example.com/content4.jpg', '화사한 디자인의 워터병.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(5, '썬캡', 25000, 21000, 'https://example.com/preview5.jpg', 'https://example.com/content5.jpg', '햇빛을 막아주는 여름용 썬캡.', 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(6, '쿨링 티셔츠', 30000, 27000, 'https://example.com/preview6.jpg', 'https://example.com/content6.jpg', '통풍이 잘되는 기능성 티셔츠.', 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false);

-- Product Option 더미 데이터
INSERT INTO product_option (
    id, product_id, option_name, extra_price, product_stock,
    created_at, created_by, updated_at, updated_by,
    is_deleted
) VALUES
-- 옵션 for product_id 1
(1, 1, '벚꽃핑크/L', 0, 100, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(2, 1, '벚꽃핑크/M', 500, 50, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- 옵션 for product_id 2
(3, 2, '연노랑/L', 0, 120, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(4, 2, '연노랑/M', 1000, 30, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- 옵션 for product_id 3
(5, 3, 'S 사이즈', 0, 60, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(6, 3, 'M 사이즈', 200, 40, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- 옵션 for product_id 4
(7, 4, '투명', 0, 75, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(8, 4, '핑크톤', 500, 25, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- 옵션 for product_id 5
(9, 5, '화이트', 0, 90, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(10, 5, '네이비', 1500, 10, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),

-- 옵션 for product_id 6
(11, 6, 'L', 0, 200, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
(12, 6, 'XL', 300, 70, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false);

COMMIT;