-- Pour determiner la table faisant figurer une contrainte check dont on a le nom
SELECT 
    uc.constraint_name,
    uc.table_name,
    ucc.column_name
FROM user_constraints uc
JOIN user_cons_columns ucc
    ON uc.constraint_name = ucc.constraint_name
WHERE uc.constraint_name = 'SYS_C001323422';

-- Pour déterminer le nom d'une contrainte check
SELECT constraint_name
FROM user_constraints
WHERE table_name = 'PRODUIT'
  AND constraint_type = 'C'; -- 'C' is for check

-- Pour déterminer le nom d'une contrainte primary key
SELECT constraint_name
FROM user_constraints
WHERE table_name = 'COMMANDEAPOURRECUP'
 AND constraint_type = 'P'; -- 'P' is for primary
