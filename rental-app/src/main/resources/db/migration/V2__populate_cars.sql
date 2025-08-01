-- Populate cars table with sample data
INSERT INTO cars (name, description, price, car_type, created_at, updated_at) VALUES
('Toyota Camry', 'Reliable sedan with excellent fuel economy and comfortable interior', 45, 'sedan', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Honda Civic', 'Compact sedan perfect for city driving with great safety ratings', 40, 'sedan', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BMW 3 Series', 'Luxury sedan with premium features and sporty performance', 80, 'sedan', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Toyota RAV4', 'Popular SUV with spacious interior and excellent reliability', 60, 'suv', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Honda CR-V', 'Compact SUV with versatile cargo space and smooth ride', 55, 'suv', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ford Explorer', 'Full-size SUV with three rows of seating and powerful engine', 75, 'suv', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ford F-150', 'Best-selling pickup truck with excellent towing capacity', 70, 'truck', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Chevrolet Silverado', 'Full-size truck with advanced technology and strong performance', 75, 'truck', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Toyota Tacoma', 'Mid-size truck perfect for off-road adventures', 65, 'truck', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tesla Model 3', 'Electric sedan with instant acceleration and autopilot features', 90, 'sedan', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Populate car inventory with quantities
INSERT INTO car_inventory (car_id, quantity, created_at, updated_at) VALUES
(1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 