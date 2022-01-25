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
    pesel VARCHAR(11) NOT NULL,
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
    additional_features VARCHAR(10),
    cena NUMERIC NOT NULL
);

CREATE TABLE isp.PAKIET_INTERNETU (
    id INTEGER PRIMARY KEY,
    download  NUMERIC NOT NULL,
    upload NUMERIC NOT NULL,
    additional_features VARCHAR(10),
    cena NUMERIC NOT NULL
);

CREATE TABLE isp.GSM (
    id INTEGER  PRIMARY KEY,
    standard VARCHAR(10) NOT NULL,
    additional_features VARCHAR(10),
    cena NUMERIC NOT NULL
);

CREATE TABLE isp.UMOWA_USLUGA (
    id INTEGER PRIMARY KEY,
    oferta VARCHAR(10) NOT NULL,
    nabywca INTEGER  REFERENCES isp.USERS(id),
    autor INTEGER  REFERENCES isp.USERS(id),
    dokument_fk INTEGER REFERENCES isp.DOKUMENTY(id)
);

CREATE TABLE isp.URZADZENIE (
    id INTEGER PRIMARY KEY,
    nazwa VARCHAR(30) NOT NULL,
    producent VARCHAR(30) NOT NULL,
    sn VARCHAR(20) NOT NULL,
    wlasciciel INTEGER
);

CREATE TABLE isp.URZADZENIE_SIECIOWE (
    id INTEGER PRIMARY KEY,
    wlan BOOLEAN NOT NULL,
    ip_address VARCHAR(19),
    przepustowosc VARCHAR(10) NOT NULL,
    czy_dostepne BOOLEAN NOT NULL,
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
  umowa VARCHAR(15) NOT NULL,
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

CREATE TABLE isp.Wpis (
    id INTEGER PRIMARY KEY,
    data_utworzenia TIMESTAMP NOT NULL,
    opis VARCHAR(200) NOT NULL,
    przeczytane boolean,
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
CREATE SEQUENCE isp.urzadzenie_sieciowe_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.wpis_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.zamowienie_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.zlecenie_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.zlecenie_siec_seq INCREMENT BY 1 MINVALUE 1;
CREATE SEQUENCE isp.zlecenie_naprawa_seq INCREMENT BY 1 MINVALUE 1;

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
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 30, 3, '', 50);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 100, 30, '', 65);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 300, 100, '', 80);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 600, 200, '', 100);
--public ip included
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 30, 3, '0,', 65);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 100, 30, '0,', 80);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 300, 100, '0,', 95);
INSERT INTO isp.PAKIET_INTERNETU VALUES (nextval('isp.internet_seq'), 600, 200, '0,', 115);

--TV packages
--no multiroom
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 50, '', 30);
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 90, '', 45);
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 120, '', 60);
--multiroom included
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 50, '0,', 40);
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 90, '0,', 55);
INSERT INTO isp.TELEWIZJA VALUES (nextval('isp.tv_seq'), 120, '0,', 70);

--GSM packages
--no roaming
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '3G', '', 15);
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '4G', '', 25);
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '5G', '', 40);
--roaming included
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '3G', '0,', 25);
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '4G', '0,', 35);
INSERT INTO isp.GSM VALUES (nextval('isp.gsm_seq'), '5G', '0,', 50);

--Documents
INSERT INTO isp.DOKUMENTY(id, data_utworzenie, data_wygasniecia) VALUES (nextval('isp.dokument_seq'), to_timestamp('01/01/2021', 'DD/MM/YYYY'), to_timestamp('31/12/2023', 'DD/MM/YYYY'));
INSERT INTO isp.DOKUMENTY(id, data_utworzenie, data_wygasniecia) VALUES (nextval('isp.dokument_seq'), to_timestamp('01/03/2020', 'DD/MM/YYYY'), to_timestamp('28/02/2022', 'DD/MM/YYYY'));

--Contracts of employment
INSERT INTO isp.UMOWA_PRACA VALUES (nextval('isp.umowa_praca_seq'), 4000, 1, 2, 1);

--service contracts
INSERT INTO isp.UMOWA_USLUGA VALUES (nextval('isp.umowa_usluga_seq'), '2,1,0', 3, 4, 2);

--updating document with document number
UPDATE isp.DOKUMENTY SET nr_dokumentu = 'UOP/2021/1' WHERE ID = 1;
UPDATE isp.DOKUMENTY SET nr_dokumentu = 'US/2020/1' WHERE ID = 2;

--device
INSERT INTO isp.URZADZENIE VALUES (nextval('isp.urzadzenie_seq'), 'archer c6', 'TP-Link', '23469045692346', 3);
INSERT INTO isp.URZADZENIE VALUES (nextval('isp.urzadzenie_seq'), 'X515', 'ASUS', '54678041231421', 3);

--network device
INSERT INTO isp.URZADZENIE_SIECIOWE VALUES (nextval('isp.urzadzenie_sieciowe_seq'), true, '156.11.23.15', '1Gb', false, 1);

--issue entry
INSERT INTO isp.WPIS VALUES(nextval('isp.wpis_seq'), to_timestamp('10/01/2022', 'DD/MM/YYYY'), 'My network says the cable is disconnected', null, 3, null);
INSERT INTO isp.WPIS VALUES(nextval('isp.wpis_seq'), to_timestamp('10/01/2022', 'DD/MM/YYYY'), 'I get blue screens after few minutes of work', null, 3, null);
INSERT INTO isp.WPIS VALUES(nextval('isp.wpis_seq'), to_timestamp('11/01/2022', 'DD/MM/YYYY'), 'Check all cable connections and make sure they are not damaged', null, 2, null);
INSERT INTO isp.WPIS VALUES(nextval('isp.wpis_seq'), to_timestamp('12/01/2022', 'DD/MM/YYYY'), 'I did it all and it still would not work, but i have a connection directly from your cable', null, 3, null);
INSERT INTO isp.WPIS VALUES(nextval('isp.wpis_seq'), to_timestamp('12/01/2022', 'DD/MM/YYYY'), 'Okay, so we probably need to replace your router. Do you agree on 15/01?', null, 2, null);
INSERT INTO isp.WPIS VALUES(nextval('isp.wpis_seq'), to_timestamp('12/01/2022', 'DD/MM/YYYY'), 'You have date to confirm.', false, 2, 3);
--issue
INSERT INTO isp.ZLECENIE (id, creation_date, wpisy) values (nextval('isp.zlecenie_seq'), to_timestamp('10/01/2022', 'DD/MM/YYYY'), '1,3,4,5,6,');
INSERT INTO isp.ZLECENIE (id, creation_date, wpisy) values (nextval('isp.zlecenie_seq'), to_timestamp('10/01/2022', 'DD/MM/YYYY'), '2,');

--ticket for network issue
INSERT  INTO isp.ZLECENIE_SIEC VALUES (nextval('isp.zlecenie_siec_seq'), 'US/2020/1', 3, 1);

--ticket for hardware issue
INSERT INTO isp.ZLECENIE_NAPRAWA VALUES (nextval('isp.zlecenie_naprawa_seq'), '', 0, 3, 2, 0, 2);