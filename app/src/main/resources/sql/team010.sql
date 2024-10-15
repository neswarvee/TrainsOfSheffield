USE team010;

CREATE TABLE Manufacturers (
    manufacturerID INTEGER PRIMARY KEY AUTO_INCREMENT,
    manufacturerName TEXT
);

CREATE TABLE ProductCodes (
    codeID INTEGER PRIMARY KEY AUTO_INCREMENT,
    codeType ENUM('R', 'C', 'L', 'S', 'M', 'P'),
    codeValue VARCHAR(6) UNIQUE -- Ensure uniqueness
);

CREATE TABLE Products (
    productID INTEGER PRIMARY KEY AUTO_INCREMENT,
    productName TEXT,
    retailPrice DECIMAL(8, 2),
    quantityInStock INTEGER,
    gauge ENUM('OO Gauge', 'TT Gauge', 'N Gauge'),
    manufacturerID INTEGER,
    codeID INTEGER,
    FOREIGN KEY (manufacturerID) REFERENCES Manufacturers(manufacturerID),
    FOREIGN KEY (codeID) REFERENCES ProductCodes(codeID)
);

CREATE TABLE HistoricalEras (
    eraID INTEGER PRIMARY KEY AUTO_INCREMENT,
    eraCode VARCHAR(10) UNIQUE
);

CREATE TABLE TrainSets (
    trainSetID INTEGER PRIMARY KEY AUTO_INCREMENT,
    eraID INTEGER,
    productID INTEGER,
    FOREIGN KEY (eraID) REFERENCES HistoricalEras(eraID),
    FOREIGN KEY (productID) REFERENCES Products(productID) ON DELETE CASCADE
);

CREATE TABLE TrainSetComponents (
    trainSetID INTEGER,
    productID INTEGER,
    quantity INTEGER,
    PRIMARY KEY (trainSetID, productID),
    FOREIGN KEY (trainSetID) REFERENCES TrainSets(trainSetID) ON DELETE CASCADE,
    FOREIGN KEY (productID) REFERENCES Products(productID) ON DELETE CASCADE
);

CREATE TABLE Tracks (
    trackID INTEGER PRIMARY KEY AUTO_INCREMENT,
    productID INTEGER,
    FOREIGN KEY (productID) REFERENCES Products(productID) ON DELETE CASCADE
);

CREATE TABLE TrackPacks (
    trackPackID INTEGER PRIMARY KEY AUTO_INCREMENT,
    productID INTEGER,
    FOREIGN KEY (productID) REFERENCES Products(productID) ON DELETE CASCADE
);

CREATE TABLE TrackPackComponents (
    trackPackID INTEGER,
    trackID INTEGER,
    quantity INTEGER,
    PRIMARY KEY (trackPackID, trackID),
    FOREIGN KEY (trackPackID) REFERENCES TrackPacks(trackPackID) ON DELETE CASCADE,
    FOREIGN KEY (trackID) REFERENCES Tracks(trackID) ON DELETE CASCADE
);

CREATE TABLE Controllers (
    controllerID INTEGER PRIMARY KEY AUTO_INCREMENT,
    controllerType ENUM('Standard Controller', 'DCC Controller', 'DCC Elite Controller'),
    isDigital BOOLEAN,
    productID INTEGER,
    FOREIGN KEY (productID) REFERENCES Products(productID) ON DELETE CASCADE
);

CREATE TABLE Locomotives (
    locomotiveID INTEGER PRIMARY KEY AUTO_INCREMENT,
    DCCode ENUM('Analogue', 'DCC-Ready', 'DCC-Fitted', 'DCC-Sound'),
    eraID INTEGER,
    productID INTEGER,
    FOREIGN KEY (productID) REFERENCES Products(productID) ON DELETE CASCADE,
    FOREIGN KEY (eraID) REFERENCES HistoricalEras(eraID)
);

CREATE TABLE RollingStocks (
    rollingStockID INTEGER PRIMARY KEY AUTO_INCREMENT,
    BRStandardMark ENUM('Mark 1', 'Mark 2', 'Mark 3', 'Mark 4'),
    companyMark ENUM('LMS', 'LNER', 'GWR', 'SR'),
    rollingStockType ENUM('Carriage', 'Wagon'),
    eraID INTEGER,
    productID INTEGER,
    FOREIGN KEY (productID) REFERENCES Products(productID) ON DELETE CASCADE,
    FOREIGN KEY (eraID) REFERENCES HistoricalEras(eraID)
);

CREATE TABLE Postcodes (
    postcodeID INTEGER PRIMARY KEY AUTO_INCREMENT,
    postcode TEXT(8)
);

CREATE TABLE Streets (
    streetID INTEGER PRIMARY KEY AUTO_INCREMENT,
    street TEXT(100)
);

CREATE TABLE Cities (
    cityID INTEGER PRIMARY KEY AUTO_INCREMENT,
    city TEXT(100)
);

CREATE TABLE Addresses (
    addressID INTEGER PRIMARY KEY AUTO_INCREMENT,
    streetID INTEGER,
    cityID INTEGER,
    postcodeID INTEGER,
    FOREIGN KEY (streetID) REFERENCES Streets(streetID),
    FOREIGN KEY (cityID) REFERENCES Cities(cityID),
    FOREIGN KEY (postcodeID) REFERENCES Postcodes(postcodeID)
);

CREATE TABLE Users (
    userID VARCHAR (50) NOT NULL PRIMARY KEY,
    email VARCHAR (254) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    passwordHash VARCHAR(64) NOT NULL,
    accountLocked BOOLEAN DEFAULT FALSE,
    firstName TEXT(50),
    surname TEXT(50),
    role ENUM('Customer', 'Staff', 'Manager'),
    addressID INTEGER,
    FOREIGN KEY (addressID) REFERENCES Addresses(addressID)
);

CREATE TABLE Orders (
    orderID INTEGER PRIMARY KEY AUTO_INCREMENT,
    order_date DATE,
    status ENUM("Pending", "Confirmed", "Fulfilled"),
    totalCost DECIMAL(10, 2), -- 10 digits with 2 decimal places
    userID VARCHAR (50),
    FOREIGN KEY (userID) REFERENCES Users(userID)
);

CREATE TABLE OrderLines (
    orderLineID INTEGER PRIMARY KEY AUTO_INCREMENT,
    quantity INTEGER,
    productID INTEGER,
    orderID INTEGER,
    FOREIGN KEY (productID) REFERENCES Products(productID) ON DELETE CASCADE,
    FOREIGN KEY (orderID) REFERENCES Orders(orderID) ON DELETE CASCADE
);

CREATE TABLE BankDetails (
    userID VARCHAR (50),
    cardType TEXT,
    cardHolderName TEXT,
    cardNumber TEXT,
    cardExpiryDate TEXT,
    cardCVV TEXT,
    PRIMARY KEY (userID),
    FOREIGN KEY (userID) REFERENCES Users(userID)
);