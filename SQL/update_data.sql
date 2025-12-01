-- Ligne 1
UPDATE LotProduit
SET QuantiteDisponibleP = 20
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('10-OCT-25','DD-MON-RR')
  AND idProduit = 9;

-- Ligne 2
UPDATE LotProduit
SET QuantiteDisponibleP = 15
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('10-OCT-25','DD-MON-RR')
  AND idProduit = 1;

-- Ligne 3
UPDATE LotProduit
SET QuantiteDisponibleP = 30
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('15-SEP-25','DD-MON-RR')
  AND idProduit = 3;

-- Ligne 4
UPDATE LotProduit
SET QuantiteDisponibleP = 25
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('18-OCT-25','DD-MON-RR')
  AND idProduit = 5;

-- Ligne 5
UPDATE LotProduit
SET QuantiteDisponibleP = 28
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('25-OCT-25','DD-MON-RR')
  AND idProduit = 7;

-- Ligne 6
UPDATE LotProduit
SET QuantiteDisponibleP = 12
WHERE ModeConditionnement = 'preconditionne' 
  AND PoidsUnitaire = 0.5
  AND DateReceptionP = TO_DATE('20-OCT-25','DD-MON-RR')
  AND idProduit = 6;

-- Ligne 7
UPDATE LotProduit
SET QuantiteDisponibleP = 18
WHERE ModeConditionnement = 'preconditionne' 
  AND PoidsUnitaire = 0.5
  AND DateReceptionP = TO_DATE('12-OCT-25','DD-MON-RR')
  AND idProduit = 2;

-- Ligne 1
UPDATE LotProduit
SET QuantiteDisponibleP = 20
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('10-OCT-25','DD-MON-RR')
  AND idProduit = 9;

-- Ligne 2
UPDATE LotProduit
SET QuantiteDisponibleP = 15
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('10-OCT-25','DD-MON-RR')
  AND idProduit = 1;

-- Ligne 3
UPDATE LotProduit
SET QuantiteDisponibleP = 30
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('15-SEP-25','DD-MON-RR')
  AND idProduit = 3;

-- Ligne 4
UPDATE LotProduit
SET QuantiteDisponibleP = 25
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('18-OCT-25','DD-MON-RR')
  AND idProduit = 5;

-- Ligne 5
UPDATE LotProduit
SET QuantiteDisponibleP = 28
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('25-OCT-25','DD-MON-RR')
  AND idProduit = 7;

-- Ligne 6
UPDATE LotProduit
SET QuantiteDisponibleP = 12
WHERE ModeConditionnement = 'preconditionne' 
  AND PoidsUnitaire = 0.5
  AND DateReceptionP = TO_DATE('20-OCT-25','DD-MON-RR')
  AND idProduit = 6;

-- Ligne 7
UPDATE LotProduit
SET QuantiteDisponibleP = 18
WHERE ModeConditionnement = 'preconditionne' 
  AND PoidsUnitaire = 0.5
  AND DateReceptionP = TO_DATE('12-OCT-25','DD-MON-RR')
  AND idProduit = 2;

UPDATE LotContenant
SET QUANTITEDISPONIBLEC = 70
WHERE QUANTITEDISPONIBLEC IS NULL;

commit;