CREATE TABLE AdressePComplete (
    Latitude FLOAT NOT NULL
        CONSTRAINT Clatitude CHECK (Latitude >= -90.0 AND Latitude <= 90.0),
    Longitude FLOAT NOT NULL
        CONSTRAINT CLongitude CHECK (Longitude >= -180.0 AND Longitude <= 180.0),
        
    PRIMARY KEY (Latitude, Longitude) 
);

CREATE TABLE Producteur (
    idProducteur INT PRIMARY KEY,
    
    email VARCHAR(30),
    NomProducteur VARCHAR(30),
    PrenomProducteur VARCHAR(30),
    
    Latitude FLOAT NOT NULL,
    Longitude FLOAT NOT NULL,
    
    CONSTRAINT FK_Producteur_Adresse
        FOREIGN KEY (Latitude, Longitude) 
        REFERENCES AdressePComplete(Latitude, Longitude)
        ON DELETE Cascade
);

CREATE TABLE Activité(
    TypeActivité VARCHAR(30) PRIMARY KEY
);

CREATE TABLE ProducteurAPourActivité(
    idProducteur int,
    TypeActivité VARCHAR(30),

    PRIMARY KEY (idProducteur, TypeActivité),

    CONSTRAINT fk_idProducteur
        FOREIGN KEY (idProducteur) 
        REFERENCES Producteur(idProducteur)
        ON DELETE Cascade,
    
    
    CONSTRAINT fk_typeActivité
        FOREIGN KEY (TypeActivité) 
        REFERENCES Activité(TypeActivité)
        ON DELETE Cascade
);

-- DROP TABLE ProducteurAPourActivité;
-- DROP TABLE Producteur;
-- DROP TABLE Activité;
-- DROP TABLE AdressePComplete;



