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

CREATE TABLE isp.DOKUMENTY (
    id INTEGER PRIMARY KEY,
    nr_dokumentu VARCHAR(15),
    data_utworzenie TIMESTAMP NOT NULL,
    data_wygasniecia TIMESTAMP NOT NULL
);

CREATE TABLE isp.FAKTURY (
    id INTEGER PRIMARY KEY,
    kwota NUMERIC,
    nabywca INTEGER REFERENCES ISP.USERS(id),
    dokument_fk INTEGER REFERENCES isp.DOKUMENTY(id)
);

CREATE TABLE isp.ZAMOWIENIE (
    id INTEGER PRIMARY KEY,
    kwota NUMERIC,
    czesci VARCHAR(20),
    dokument_fk INTEGER REFERENCES isp.DOKUMENTY(id)
);

CREATE TABLE isp.WPISY_KASOWE (
    id INTEGER PRIMARY KEY,
    kwota NUMERIC,
    wykonawca  INTEGER REFERENCES isp.USERS(id),
    dokument_fk INTEGER REFERENCES isp.DOKUMENTY(id)
);

CREATE TABLE isp.UMOWA_PRACA (
    id INTEGER PRIMARY KEY,
    wynagrodzenie NUMERIC,
    role INTEGER NOT NULL,
    pracownik INTEGER REFERENCES isp.USERS(id),
    dokument_fk INTEGER REFERENCES isp.DOKUMENTY(id)
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

CREATE TABLE isp.URZADZENIE (
    id INTEGER PRIMARY KEY,
    nazwa VARCHAR(30) NOT NULL,
    producent VARCHAR(30) NOT NULL,
    sn VARCHAR(20) NOT NULL
);

CREATE TABLE isp.URZADZENIE_SIECIOWE (
    id INTEGER PRIMARY KEY,
    wlan BOOLEAN NOT NULL,
    przepustowosc VARCHAR(10) NOT NULL,
    czy_dostepne BOOLEAN NOT NULL,
    wlasciciel INTEGER,
    urzadzenie_fk INTEGER REFERENCES isp.URZADZENIE(id)
);

CREATE TABLE isp.CZESC_KOMPUTEROWA (
  id INTEGER PRIMARY KEY,
  port VARCHAR(20) NOT NULL,
  przeznaczenie VARCHAR(30) NOT NULL,
  koszt NUMERIC NOT NULL,
  urzadzenie_fk INTEGER REFERENCES isp.URZADZENIE(id)
);

CREATE TABLE isp.ZLECENIE (
  id INTEGER PRIMARY KEY,
  creation_date TIMESTAMP NOT NULL,
  close_date TIMESTAMP,
  wpisy VARCHAR(20)
);

CREATE TABLE isp.ZLECENIE_SIEC (
  id INTEGER PRIMARY KEY,
  opis VARCHAR(100),
  sprzet VARCHAR(20),
  klient INTEGER REFERENCES isp.USERS(id),
  zlecenie_fk INTEGER REFERENCES isp.ZLECENIE(id)
);

CREATE TABLE isp.ZLECENIE_NAPRAWA (
    id INTEGER PRIMARY KEY,
    uslugi VARCHAR(20),
    kwota NUMERIC,
    wlasciciel INTEGER REFERENCES isp.USERS(id),
    urzadzenie INTEGER REFERENCES isp.URZADZENIE(id),
    zamowienie INTEGER,
    zlecenie_fk INTEGER REFERENCES isp.ZLECENIE(id)
);

CREATE TABLE isp.WPIS (
    id INTEGER PRIMARY KEY,
    data_utworzenia TIMESTAMP,
    opis VARCHAR(100),
    autor INTEGER REFERENCES isp.USERS(id)
);

CREATE TABLE isp.POWIADOMIENIE (
    id INTEGER PRIMARY KEY,
    data_utworzenia TIMESTAMP,
    opis VARCHAR(100),
    przeczytane boolean NOT NULL,
    autor INTEGER REFERENCES isp.USERS(id),
    odbiorca INTEGER REFERENCES isp.USERS(id)
);

--Sequences for id
CREATE SEQUENCE isp.user_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.address_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.faktury_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.umowa_praca_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.umowa_usluga_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.dokument_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.internet_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.tv_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.gsm_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.urzadzenie_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.wpis_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.zamowienie_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.zlecenie_naprawa_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.powiadomienie_seq INCREMENT BY 1 MINVALUE 1;

--Addresses
INSERT INTO isp.ADDRESS VALUES (nextval('isp.address_seq'), 'Kielce', 'aleja Panstwa polskiego', '15/609', '25-109');
INSERT INTO isp.ADDRESS VALUES (nextval('isp.address_seq'), 'Kielce', 'aleja Panstwa polskiego', '15/610', '25-109');
INSERT INTO isp.ADDRESS VALUES (nextval('isp.address_seq'), 'Kielce', 'aleja Panstwa polskiego', '15/608', '25-109');
INSERT INTO isp.ADDRESS VALUES (nextval('isp.address_seq'), 'Kielce', 'aleja Panstwa polskiego', '15/607', '25-109');
INSERT INTO isp.ADDRESS VALUES (nextval('isp.address_seq'), 'Kielce', 'aleja Panstwa polskiego', '15/606', '25-109');
--Users
INSERT INTO isp.USERS VALUES (nextval('isp.user_seq'), 'admin', 'admin', 'Mirek', 'Jakistam', '999999999', 'mail@mail.here', '12345678909', '123456789', 0, 1);
INSERT INTO isp.USERS VALUES (nextval('isp.user_seq'), 'admin2', 'admin', 'Mirek', 'Jakistam', '999999999', 'mail@mail.here', '12345678902', '123456789', 1, 2);
INSERT INTO isp.USERS VALUES (nextval('isp.user_seq'), 'admin3', 'admin', 'Mirek', 'Jakistam', '999999999', 'mail@mail.here', '12345678903', '123456789', 2, 3);
INSERT INTO isp.USERS VALUES (nextval('isp.user_seq'), 'admin4', 'admin', 'Mirek', 'Jakistam', '999999999', 'mail@mail.here', '12345678904', '123456789', 3, 4);

--Internet packages
--no public ip
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 30, 3, 'false', 50);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 100, 30, 'false', 65);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 300, 100, 'false', 80);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 600, 200, 'false', 100);
--public ip included
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 30, 3, 'true', 65);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 100, 30, 'true', 80);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 300, 100, 'true', 95);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 600, 200, 'true', 115);

--TV packages
--no multiroom
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 50, 'false', 30);
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 90, 'false', 45);
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 120, 'false', 60);
--multiroom included
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 50, 'true', 40);
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 90, 'true', 55);
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 120, 'true', 70);

--GSM packages
--no roaming
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '3G', 'false', 15);
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '4G', 'false', 25);
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '5G', 'false', 40);
--roaming included
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '3G', 'true', 25);
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '4G', 'true', 35);
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '5G', 'true', 50);

--Documents
INSERT INTO isp.DOKUMENTY(id, data_utworzenie, data_wygasniecia) VALUES (nextval('isp.dokument_seq'), to_timestamp('01/01/2021', 'DD/MM/YYYY'), to_timestamp('31/12/2023', 'DD/MM/YYYY'));

--Contracts of employment
INSERT INTO isp.UMOWA_PRACA VALUES (nextval('isp.umowa_praca_seq'), 4000, 1, 2, 1);

UPDATE isp.DOKUMENTY SET nr_dokumentu = 'UOP/2021/1' WHERE ID = 1;

