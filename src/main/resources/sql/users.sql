
INSERT INTO users (name, email, password, created_at, updated_at) 
VALUES 
('John Doe', 'john.doe@example.com', 'password123', '2024-12-18 10:00:00', '2024-12-18 10:00:00'),
('Jane Smith', 'jane.smith@example.com', 'securepass', '2024-12-18 11:00:00', '2024-12-18 11:00:00'),
('Emily Davis', 'emily.davis@example.com', 'mypassword', '2024-12-18 12:00:00', '2024-12-18 12:00:00')

INSERT INTO user_roles (user_id, roles) 
VALUES 
(1, 'USER'),
(2, 'ADMIN'),
(3, 'USER'),
