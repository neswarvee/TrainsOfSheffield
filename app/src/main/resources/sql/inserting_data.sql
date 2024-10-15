 -- Manufacturers
INSERT INTO Manufacturers (manufacturerID, manufacturerName) VALUES
(1, 'Bachmann'),
(2, 'Dapol'),
(3, 'Hornby');

-- ProductCodes
INSERT INTO ProductCodes (codeID, codeType, codeValue) VALUES
(1, 'C', '9284'),
(2, 'C', '837'),
(3, 'C', '12837'),
(4, 'R', 9289),
(5, 'R', 781),
(6, 'R', 2678),
(7, 'P', 8121),
(8, 'P', 12345),
(9, 'P', 54321),
(10, 'L', 9283),
(11, 'L', 7289),
(12, 'L', 2345),
(13, 'L', 25290),
(14, 'S', 287),
(15, 'S', 7729),
(16, 'S', 15902),
(17, 'S', 8422),
(18, 'M', 442),
(19, 'M', 777),
(20, 'M', 613);

-- Products
INSERT INTO Products (productID, productName, retailPrice, quantityInStock, gauge, manufacturerID, codeID) VALUES
-- controllers
(1, 'Hornby R7229 Analogue Train and Accessory Controller', 17.54, 100, 'OO Gauge', 3, 1),
(2, 'Hornby R8012 HM2000 High output power & speed controller', 129.00, 75, 'OO Gauge', 3, 2),
(3, 'Bachmann Branchline 36-500-PO24 EZ Command DCC controller', 51.00, 50, 'TT Gauge', 3, 3),
-- tracks
(4, 'Bachmann track piece - Double Straight', 4.00, 40, 'OO Gauge', 1, 4),
(5, 'Dapol track piece - Single Curve - 2nd radius', 7.00, 40, 'TT Gauge', 2, 5),
(6, 'Hornby track piece - Right-Hand Crossover', 9.99, 50, 'N Gauge', 3, 6),
-- track packs
(7, 'Track Pack A', 22.00, 5, 'OO Gauge', 1, 7),
(8, 'Track Pack B', 26.00, 5, 'TT Gauge', 2, 8),
(9, 'Track Pack C', 19.00, 5, 'N Gauge', 3, 9),
-- locomotives
(10, 'Class A3 "Flying Scotsman" Locomotive', 159.99, 10, 'OO Gauge', 1, 10),
(11, 'Class 20 Shunter', 143.99, 12, 'TT Gauge', 2, 11),
(12, 'Class 7P Britannia', 172.49, 8, 'OO Gauge', 3, 12),
(13, 'Class 55 Deltic Diesel', 123.99, 5, 'N Gauge', 2, 13),
-- rolling stocks
(14, 'Open First Carriage', 186.00, 8, 'OO Gauge', 1, 14),
(15, 'Horse Box Wagon', 173.99, 7, 'TT Gauge', 3, 15),
(16, 'Buddet Car Carriage', 128.99, 3, 'OO Gauge', 2, 16),
(17, '20t Hopper Wagon', 134.49, 4, 'N Gauge', 1, 17),
-- train sets
(18, 'Train set A', 278.99, 2, 'OO Gauge', 2, 18),
(19, 'Train set B', 309.99, 3, 'OO Gauge', 3, 19),
(20, 'Train set C', 333.99, 1, 'TT Gauge', 1, 20);


-- HistoricalEras
INSERT INTO HistoricalEras (eraID, eraCode) VALUES
(1, 'Era 1'),
(2, 'Era 1-2'),
(3, 'Era 2'),
(4, 'Era 2-3'),
(5, 'Era 3'),
(6, 'Era 3-4'),
(7, 'Era 4'),
(8, 'Era 4-5'),
(9, 'Era 5'),
(10, 'Era 5-6'),
(11, 'Era 6'),
(12, 'Era 6-7'),
(13, 'Era 7'),
(14, 'Era 7-8'),
(15, 'Era 8'),
(16, 'Era 8-9'),
(17, 'Era 9'),
(18, 'Era 9-10'),
(19, 'Era 10'),
(20, 'Era 10-11'),
(21, 'Era 11');

-- TrainSets
INSERT INTO TrainSets (trainSetID, eraID, productID) VALUES
(1, 3, 18),
(2, 9, 19),
(3, 17, 20);

-- TrainSetComponents
INSERT INTO TrainSetComponents (trainSetID, productID, quantity) VALUES
(1, 1, 1),
(1, 7, 1),
(1, 16, 2),
(2, 2, 1),
(2, 8, 1),
(2, 12, 2),
(3, 3, 1),
(3, 9, 1),
(3, 14, 2);

-- Tracks
INSERT INTO Tracks (trackID, productID) VALUES
(1, 4),
(2, 5),
(3, 6);

-- TrackPacks
INSERT INTO TrackPacks (trackPackID, productID) VALUES
(1, 7),
(2, 8),
(3, 9);

-- TrackPackComponents
INSERT INTO TrackPackComponents (trackPackID, trackID, quantity) VALUES
(1, 1, 4),
(1, 2, 2),
(1, 3, 2),
(2, 1, 6),
(2, 3, 2),
(3, 2, 8),
(3, 3, 4);

-- Controllers
INSERT INTO Controllers (controllerID, controllerType, isDigital, productID) VALUES
(1, 'Standard Controller', false, 1),
(2, 'DCC Controller', true, 2),
(3, 'DCC Elite Controller', true, 3);

-- Locomotives
INSERT INTO Locomotives (locomotiveID, DCCode, eraID, productID) VALUES
(1, 'Analogue', 17, 10),
(2, 'DCC-Ready', 16, 11),
(3, 'DCC-Fitted', 15, 12),
(4, 'DCC-Sound', 14, 13);

-- RollingStocks
INSERT INTO RollingStocks (rollingStockID, BRStandardMark, companyMark, rollingStockType, eraID, productID) VALUES
(1, 'Mark 1', 'LMS', 'Carriage', 8, 14),
(2, 'Mark 2', 'LNER', 'Wagon', 12, 15),
(3, 'Mark 3', 'GWR', 'Carriage', 3, 16),
(4, 'Mark 4', 'SR', 'Wagon', 18, 17);

-- Postcodes
INSERT INTO Postcodes (postcode) VALUES ('S10 2TN'), ('S1 4DA'), ('S11 8NT');

-- Streets
INSERT INTO Streets (street) VALUES ('Glossop Road'), ('West Street'), ('Ecclesall Road');

-- Cities
INSERT INTO Cities (city) VALUES ('Sheffield'), ('Sheffield'), ('Sheffield');

-- Addresses
INSERT INTO Addresses (streetID, cityID, postcodeID) VALUES (1, 1, 1), (2, 2, 2), (3, 3, 3);

-- Users
INSERT INTO Users (userID, email, salt, passwordHash, accountLocked, firstName, surname, role, addressID) VALUES
('8b5de856-b8ef-4865-b310-b3cd599ff9fa', 'john.doe@example.com', 'DXICAh5VTq1BUVadkHYO7A==', 
'3aae88afcbb2cb60c5df58d093477fc99c712fe4f0ddb92bad66d6f4428a1ddd', FALSE, 'John', 'Doe', 'Customer', 1),
('18bbc556-6541-426f-9ecf-5acfbe915b44', 'jane.smith@example.com', 'EzykbvZWDz4g1rdR5r7hGw==', 
'f9b61417dfa53197fc6d9cfa285cdc0034ec6e8c3333b601449ff3518ca59438', FALSE, 'Jane', 'Smith', 'Staff', 2),
('07419a1e-551e-48ca-bda9-741ed360f0ed', 'alice.jackson@example.com', 'BNFD5stT4+2eVYmKFeP/DA==', 
'66ad555c02e55e1f4a7f784a7effb044f54af6d792b4085e728978148767849a', FALSE, 'Alice', 'Jackson', 'Manager', 3);

-- Orders
INSERT INTO Orders (orderID, order_date, status, totalCost, userID) VALUES
(1, DATE '2023-11-12', 'Pending', 14.99, '8b5de856-b8ef-4865-b310-b3cd599ff9fa'),
(2, DATE '2023-11-12', 'Confirmed', 10.00, '8b5de856-b8ef-4865-b310-b3cd599ff9fa'),
(3, DATE '2023-11-12', 'Fulfilled', 10.00, '8b5de856-b8ef-4865-b310-b3cd599ff9fa'),
(4, DATE '2023-11-12', 'Confirmed', 69.00, '8b5de856-b8ef-4865-b310-b3cd599ff9fa');

-- OrderLines
INSERT INTO OrderLines (orderLineID, quantity, productID, orderID) VALUES
(1, 1, 5, 1),
(2, 3, 9, 1),
(3, 2, 13, 2),
(4, 1, 10, 2),
(5, 4, 2, 3),
(6, 1, 18, 3),
(7, 3, 9, 3),
(8, 1, 1, 4);