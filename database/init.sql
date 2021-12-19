DROP schema public cascade;

CREATE schema isp;
GRANT ALL ON SCHEMA isp TO "admin";
ALTER ROLE "admin" IN DATABASE "isp" SET search_path TO isp;

CREATE TABLE isp.ADDRESS (
     id INTEGER PRIMARY KEY,
     city VARCHAR(50),
     street VARCHAR(50),
     house_number VARCHAR(10),
     code VARCHAR(10)
);

CREATE TABLE isp.USERS (
    id INTEGER PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(50),
    surname VARCHAR(50),
    phone VARCHAR(9),
    mail VARCHAR(50),
    pesel VARCHAR(11) UNIQUE NOT NULL,
    ID_card VARCHAR(9),
    role INTEGER NOT NULL,
    address INTEGER REFERENCES isp.ADDRESS(id)
);

CREATE TABLE isp.FAKTURY (
    id INTEGER PRIMARY KEY,
    creation_date TIMESTAMP,
    dueto_date TIMESTAMP,
    kwota NUMERIC,
    nabywca INTEGER REFERENCES ISP.USERS(id),
    sprzedawca INTEGER REFERENCES ISP.USERS(id)
);

CREATE TABLE isp.WPISY_KASOWE (
    id INTEGER PRIMARY KEY,
    kwota NUMERIC,
    wykonawca  INTEGER REFERENCES isp.USERS(id)
);

CREATE TABLE isp.UMOWA_PRACA (
    id INTEGER PRIMARY KEY,
    wynagrodzenie NUMERIC,
    role INTEGER NOT NULL,
    pracownik INTEGER REFERENCES isp.USERS(id)
);

CREATE TABLE isp.ZLECENIE (
    id INTEGER PRIMARY KEY,
    czesci VARCHAR(50)  --zrobic na serializacji listy?
);

CREATE TABLE isp.TELEWIZJA (
    id INTEGER PRIMARY KEY,
    lista_kanalow INTEGER NOT NULL,
    multiroom BOOLEAN NOT NULL,
    cena NUMERIC NOT NULL
);

CREATE TABLE isp.PAKIET_INTERNETU (
    id INTEGER PRIMARY KEY,
    download  NUMERIC NOT NULL,
    upload NUMERIC NOT NULL,
    publiczne_ip BOOLEAN NOT NULL,
    cena NUMERIC NOT NULL
);

CREATE TABLE isp.GSM (
    id INTEGER  PRIMARY KEY,
    standard VARCHAR(10) NOT NULL,
    roaming BOOLEAN NOT NULL,
    cena NUMERIC NOT NULL
);

CREATE TABLE isp.UMOWA_USLUGA (
    id INTEGER PRIMARY KEY,
    oferta VARCHAR(10) NOT NULL,
    nabywca INTEGER  REFERENCES isp.USERS(id),
    autor INTEGER  REFERENCES isp.USERS(id)
);



CREATE SEQUENCE isp.user_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.address_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.dokumenty_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.oferta_seq INCREMENT BY 1 MINVALUE 1;


INSERT INTO isp.ADDRESS VALUES (nextval('isp.address_seq'), 'Kielce', 'Panstwa polskiego', '15/609', '25-109');
INSERT INTO isp.USERS VALUES (nextval('isp.user_seq'), 'admin', 'admin', 'Mirek', 'Jakistam', '999999999', 'mail@mail.here', '12345678909', '123456789', 0, 1);
