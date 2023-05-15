CREATE TABLE IF NOT EXISTS `drone` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `serial_number` VARCHAR(100) UNIQUE,
    `model` VARCHAR(100),
    `state` VARCHAR(100),
    `weight_limit` int,
    `current_weight` int,
    `battery` int
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO drone (serial_number, model, state, weight_limit, current_weight, battery)
VALUES ('000001', 'MIDDLEWEIGHT', 'IDLE', 400, 0, 100);
INSERT INTO drone (serial_number, model, state, weight_limit, current_weight, battery)
VALUES ('000002', 'HEAVYWEIGHT', 'IDLE', 500, 0, 100);

CREATE TABLE IF NOT EXISTS `medication` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(100) UNIQUE,
    `name` VARCHAR(100),
    `image` VARCHAR(100),
    `weight` int
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO medication (code, name, image, weight)
VALUES ('med_001', 'Some-light-medication', 'www.images.com/image01.png', 100);
INSERT INTO medication (code, name, image, weight)
VALUES ('med_002', 'Some-heavy-medication', 'www.images.com/image02.png', 300);

CREATE TABLE IF NOT EXISTS `event_log` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `event_type` VARCHAR(20),
    `action` VARCHAR(200),
    `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS `drones_medications` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `drone_id` INT,
    `medication_id` INT,
    FOREIGN KEY (drone_id) REFERENCES drone(id),
    FOREIGN KEY (medication_id) REFERENCES medication(id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS `route` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `drone_id` INT,
    `total_distance` INT,
    `passed_distance` INT,
    FOREIGN KEY (drone_id) REFERENCES drone(id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;