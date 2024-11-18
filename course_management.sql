use coursemanagement_db;
alter table language
drop column code; 
CREATE TABLE category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    description TEXT
    );

CREATE TABLE language (
    language_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30)
    );

CREATE TABLE instructor (
    instructor_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    email VARCHAR(255)
 
);

CREATE TABLE course (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    description TEXT,
    price DECIMAL(10, 2),
    duration INT,
	video_url VARCHAR(255),
    category_id INT,
    language_id INT,
    instructor_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (language_id) REFERENCES language(language_id),
    FOREIGN KEY (instructor_id) REFERENCES instructor(instructor_id)
);

INSERT INTO category ( name, description) VALUES
('Lập trình', 'Các khóa học về lập trình và phát triển phần mềm'),
('Khoa học dữ liệu', 'Học về phân tích, xử lý và trực quan hóa dữ liệu'),
('Trí tuệ nhân tạo', 'Khóa học về AI và machine learning'),
('An ninh mạng', 'Học cách bảo mật và phòng chống tấn công mạng'),
('Quản lý hệ thống', 'Các khóa học về quản trị hệ thống và mạng máy tính'),
('Thiết kế web', 'Học về xây dựng và phát triển trang web'),
('Điện toán đám mây', 'Khóa học về công nghệ điện toán đám mây');
INSERT INTO language (name, code) VALUES
('Java', 'java'),
('Python', 'python'),
('PHP', 'php'),
('JavaScript', 'javascript'),
('Nodejs', 'nodejs'),
('C++', 'cpp'),
('Ruby', 'ruby');

INSERT INTO instructor (name, email) VALUES
('Nguyễn Văn A', 'nguyenvana@example.com'),
('Trần Thị B', 'tranthib@example.com'),
('Lê Văn C', 'levanc@example.com'),
('Phạm Minh D', 'phamminhd@example.com'),
('Vũ Thị E', 'vuthie@example.com'),
('Hoàng Văn F', 'hoangvanf@example.com'),
('Đỗ Minh G', 'doming@example.com');

INSERT INTO course (title, description, price, duration, category_id, language_id, instructor_id, video_url) VALUES
('Lập trình Java cơ bản', 'Học các khái niệm cơ bản trong lập trình Java.', 500000, 20, 1, 1, 1, 'https://example.com/java-co-ban'),
('Phân tích dữ liệu với Python', 'Khóa học từ cơ bản đến nâng cao với Python cho khoa học dữ liệu.', 800000, 25, 2, 2, 2, 'https://example.com/python-data-analysis'),
('Xây dựng website với PHP', 'Học cách phát triển website từ đầu với PHP.', 900000, 30, 1, 3, 3, 'https://example.com/php-website-development'),
('JavaScript cho người mới', 'Các khái niệm cơ bản về JavaScript và ứng dụng trong frontend.', 700000, 18, 1, 4, 4, 'https://example.com/javascript-basics'),
('Xây dựng API với Node.js', 'Học cách phát triển RESTful API với Node.js.', 750000, 24, 1, 5, 5, 'https://example.com/nodejs-api');


DELIMITER $$

DROP PROCEDURE IF EXISTS search_courses $$
CREATE PROCEDURE search_courses(
    IN p_categoryId INT,
    IN p_languageId INT,
    IN p_instructorId INT,
    IN p_keyword VARCHAR(255)
)
BEGIN
    DECLARE category_exists boolean default true;
    DECLARE language_exists boolean default true;
    DECLARE instructor_exists boolean default true;
    
    
    -- Kiểm tra sự tồn tại của category_id nếu nó khác 0
    IF p_categoryId IS NOT NULL AND p_categoryId != 0 THEN
        SELECT COUNT(*) INTO category_exists 
        FROM category 
        WHERE category_id = p_categoryId;

        -- Nếu category không tồn tại, dừng thủ tục
        IF category_exists = false THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Category ID does not exist';
        END IF;
    END IF;

    -- Kiểm tra sự tồn tại của language_id nếu nó khác 0
    IF p_languageId IS NOT NULL AND p_languageId != 0 THEN
        SELECT COUNT(*) INTO language_exists 
        FROM language 
        WHERE language_id = p_languageId;

        -- Nếu language không tồn tại, dừng thủ tục
        IF language_exists = false THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Language ID does not exist';
        END IF;
    END IF;

    -- Kiểm tra sự tồn tại của instructor_id nếu nó khác 0
    IF p_instructorId IS NOT NULL AND p_instructorId != 0 THEN
        SELECT COUNT(*) INTO instructor_exists 
        FROM instructor 
        WHERE instructor_id = p_instructorId;

        -- Nếu instructor không tồn tại, dừng thủ tục
        IF instructor_exists = false THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Instructor ID does not exist';
        END IF;
    END IF;


    -- Truy vấn khóa học nếu tất cả các ID hợp lệ
    SELECT 
        c.course_id, c.title, c.description, c.price, 
        c.category_id, c.language_id, c.instructor_id,
        c.created_at, c.updated_at
    FROM course c
    WHERE 
        (p_categoryId IS NULL OR p_categoryId = 0 OR c.category_id = p_categoryId)
        AND (p_languageId IS NULL OR p_languageId = 0 OR c.language_id = p_languageId)
        AND (p_instructorId IS NULL OR p_instructorId = 0 OR c.instructor_id = p_instructorId)
        AND (p_keyword IS NULL OR p_keyword = '' OR c.title LIKE CONCAT('%', p_keyword, '%'));
END $$

DELIMITER ;
CALL search_courses(null, null, null, 'Java');

DELIMITER $$

DROP PROCEDURE IF EXISTS insert_course $$

CREATE PROCEDURE insert_course(
    IN p_title VARCHAR(255),
    IN p_description TEXT,
    IN p_price DECIMAL(10, 2),
    IN p_categoryId INT,
    IN p_languageId INT,
    IN p_instructorId INT,
    OUT p_courseId INT
)
BEGIN
    INSERT INTO course(title, description, price, category_id, language_id, instructor_id)
    VALUES (p_title, p_description, p_price, p_categoryId, p_languageId, p_instructorId);
    
   -- lay khoa hoc vua tao
    SET p_courseId = LAST_INSERT_ID();
END $$

DELIMITER ;