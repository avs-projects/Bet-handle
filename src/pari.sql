DROP TABLE IF EXISTS parijoueur;
DROP TABLE IF EXISTS parimatch;
DROP TABLE IF EXISTS pari;
DROP TABLE IF EXISTS buteur;
DROP TABLE IF EXISTS joueur_equipe;
DROP TABLE IF EXISTS matchf;
DROP TABLE IF EXISTS equipe;
DROP TABLE IF EXISTS classement;
DROP TABLE IF EXISTS parieur;

CREATE TABLE parieur (
    id_parieur SERIAL NOT NULL ,
    email character varying(64) NOT NULL,
    mdp character varying(64) NOT NULL,
    prenom character varying(64) NOT NULL,
    nom character varying(64) NOT NULL,
    pseudo character varying(64) NOT NULL,
    capital integer NOT NULL,
    date_naissance date NOT NULL,
    pret boolean NOT NULL,
    journee integer NOT NULL,
    CONSTRAINT pk_parieur PRIMARY KEY (id_parieur)
);

CREATE SEQUENCE parieur_id_seq
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
	
ALTER SEQUENCE parieur_id_seq OWNED BY parieur.id_parieur;


CREATE TABLE classement (
	classement_id SERIAL NOT NULL,
	classement_victoire integer NOT NULL,
	classement_defaite integer NOT NULL,
	classement_point integer NOT NULL,
	CONSTRAINT pk_classement PRIMARY KEY (classement_id)
);

CREATE SEQUENCE classement_id_seq
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
	
ALTER SEQUENCE classement_id_seq OWNED BY classement.classement_id;

CREATE TABLE equipe (
	equipe_id SERIAL NOT NULL,
	equipe_nom character varying(64) NOT NULL,
	equipe_note integer NOT NULL,
	classement_id integer REFERENCES classement(classement_id),
	CONSTRAINT pk_equipe PRIMARY KEY (equipe_id)
);

CREATE SEQUENCE equipe_id_seq
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

ALTER SEQUENCE equipe_id_seq OWNED BY equipe.equipe_id;

CREATE TABLE matchF (
	matchF_id SERIAL NOT NULL,
	matchF_equipe_domicile character varying(64) NOT NULL,
	matchF_equipe_exterieur character varying(64) NOT NULL,
	matchF_score_domicile integer NOT NULL,
	matchF_score_exterieur integer NOT NULL,
	matchF_joue boolean NOT NULL,
	matchF_journee integer NOT NULL,
	equipe_id_d integer REFERENCES equipe(equipe_id),
	equipe_id_e integer REFERENCES equipe(equipe_id),
	CONSTRAINT pk_matchF PRIMARY KEY (matchF_id)
);

CREATE SEQUENCE matchF_id_seq
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

ALTER SEQUENCE matchF_id_seq OWNED BY matchF.matchF_id;

CREATE TABLE joueur_equipe (
	joueureq_id SERIAL NOT NULL,
	joueureq_nom character varying(64) NOT NULL,
	joueureq_poste character varying(64) NOT NULL,
	equipe_id integer REFERENCES equipe(equipe_id),
	CONSTRAINT pk_joueur_equipe PRIMARY KEY (joueureq_id)
);

CREATE SEQUENCE joueureq_id_seq
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
	
ALTER SEQUENCE joueureq_id_seq OWNED BY joueur_equipe.joueureq_id;

CREATE TABLE buteur (
	buteur_id SERIAL NOT NULL,
	buteur_nom character varying(64) NOT NULL,
	buteur_nbBut integer NOT NULL,
	matchf_id integer REFERENCES matchf(matchf_id),
	joueureq_id integer REFERENCES joueur_equipe(joueureq_id),
	CONSTRAINT pk_buteur PRIMARY KEY (buteur_id)
);

CREATE SEQUENCE buteur_id_seq
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
	
ALTER SEQUENCE buteur_id_seq OWNED BY buteur.buteur_id;

CREATE TABLE pari (
	pari_id SERIAL NOT NULL,
	pari_cote real NOT NULL,
	pari_somme real NOT NULL,
	pari_somme_gg real NOT NULL,
	pari_joue boolean NOT NULL,
	id_parieur integer REFERENCES parieur(id_parieur),
	matchF_id integer REFERENCES matchF(matchF_id),
	CONSTRAINT pk_pari PRIMARY KEY (pari_id)
);

CREATE SEQUENCE pari_id_seq
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
	
ALTER SEQUENCE pari_id_seq OWNED BY pari.pari_id;

CREATE TABLE pariMatch (
	pari_victoire character varying(64) NOT NULL,
	pari_gagnant character varying(64)
) INHERITS(pari);

CREATE TABLE pariJoueur (
	pari_buteur character varying(64) NOT NULL,
	pari_resultat integer
)INHERITS(pari);	

ALTER TABLE classement ALTER COLUMN classement_id SET DEFAULT nextval('classement_id_seq'::regclass);
ALTER TABLE equipe ALTER COLUMN equipe_id SET DEFAULT nextval('equipe_id_seq'::regclass);
ALTER TABLE matchF ALTER COLUMN matchF_id SET DEFAULT nextval('matchF_id_seq'::regclass);
ALTER TABLE pari ALTER COLUMN pari_id SET DEFAULT nextval('pari_id_seq'::regclass);
ALTER TABLE parieur ALTER COLUMN id_parieur SET DEFAULT nextval('parieur_id_seq'::regclass);
ALTER TABLE joueur_equipe ALTER COLUMN joueureq_id SET DEFAULT nextval('joueureq_id_seq'::regclass);
ALTER TABLE buteur ALTER COLUMN buteur_id SET DEFAULT nextval('buteur_id_seq'::regclass);


INSERT INTO classement(classement_victoire, classement_defaite, classement_point)
VALUES
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0'),
('0','0','0');

INSERT INTO equipe(equipe_nom, equipe_note, classement_id)
VALUES
('Paris Saint-Germain' , '85', '1'),
('Olympique de Marseille', '79','2'),
('AS Monaco','77','3'),
('Olympique Lyonnais', '79', '4'),
('LOSC Lille','77','5'),
('Stade Rennais','76','6'),
('OGC Nice','76','7'),
('Montpellier Hérault SC','75','8'),
('RC Strasbourg Alsace','74','9'),
('Angers SCO','74','10'),
('FC Girondins de Bordeaux','75','11'),
('FC Nantes','74','12'),
('AS Saint-Etienne','73','13'),
('Stade de Reims','73','14'), 
('RC Lens','72','15'),
('Nimes Olympiques','72','16'),
('FC Metz','72','17'),
('Stade Brestois 29','72','18'),
('Dijon FCO','72','19'),
('FC Lorient','72','20');

DO $$
DECLARE t1 INTEGER; 
		t2 INTEGER; 
		ind INTEGER;
		ind2 INTEGER;
		days INTEGER;
		nmb INTEGER; 
BEGIN
	nmb = 20;
	days = 19;

	FOR i IN 0..days LOOP
		FOR j in 0..((nmb/2)-1) LOOP
			t1 = (j+i) % nmb + 1;
			t2 = ((nmb-j-1)+i) % nmb + 1;
			INSERT INTO matchf(matchf_equipe_domicile,matchf_equipe_exterieur,matchf_score_domicile,
					  matchf_score_exterieur,matchF_joue,matchF_journee,equipe_id_d,equipe_id_e)
			VALUES ((SELECT equipe_nom FROM equipe WHERE equipe_id = t1),
		   	(SELECT equipe_nom FROM equipe WHERE equipe_id = t2),0,0,false,i+1,t1,t2);
		END LOOP;
	END LOOP;
END $$;

DO $$
DECLARE t1 INTEGER; 
		t2 INTEGER; 
		ind INTEGER;
		ind2 INTEGER;
		days INTEGER;
		nmb INTEGER; 
BEGIN
	nmb = 20;
	days = 19;

	FOR i IN (days+1)..(days*2-1)LOOP
		FOR j in 0..((nmb/2)-1) LOOP
			t1 = (j+i) % nmb + 1;
			t2 = ((nmb-j-1)+i) % nmb + 1;
			INSERT INTO matchf(matchf_equipe_domicile,matchf_equipe_exterieur,matchf_score_domicile,
					  matchf_score_exterieur,matchF_joue,matchF_journee,equipe_id_d,equipe_id_e)
			VALUES ((SELECT equipe_nom FROM equipe WHERE equipe_id = t2),
		   	(SELECT equipe_nom FROM equipe WHERE equipe_id = t1),0,0,false,i+1,t2,t1);
		END LOOP;
	END LOOP;
END $$;

INSERT INTO joueur_equipe (joueureq_nom,joueureq_poste,equipe_id)
VALUES
('Mbappe','ATT','1'),
('Neymar','ATT','1'),
('Di Maria','ATT','1'),
('Icardi','ATT','1'),
('Verratti','MIL','1'),
('Pereira','MIL','1'),
('Bernat','DEF','1'),
('Kimpembe','DEF','1'),
('Marquinhos','DEF','1'),
('Florenzi','DEF','1'),
('Navas','G','1'),

('Payet','ATT','2'),
('Thauvin','ATT','2'),
('Benedetto','ATT','2'),
('Sanson','MIL','2'),
('Rongier','MIL','2'),
('Kamara','MIL','2'),
('Amavi','MIL','2'),
('Caleta-Car','DEF','2'),
('Alvaro','DEF','2'),
('Sakai','DEF','2'),
('Mandanda','G','2'),

('Yedder','ATT','3'),
('Martins','ATT','3'),
('Volland','ATT','3'),
('Golovin','MIL','3'),
('Tchouaméni','MIL','3'),
('Fabregas','MIL','3'),
('Ballo-Touré','DEF','3'),
('Badiashile','DEF','3'),
('Disasi','DEF','3'),
('Aguilar','DEF','3'),
('Lecomte','G','3'),

('Depay','ATT','4'),
('Dembele','ATT','4'),
('Ekambi','ATT','4'),
('Aouar','MIL','4'),
('Paqueta','MIL','4'),
('Mendes','MIL','4'),
('Cornet','DEF','4'),
('Marcelo','DEF','4'),
('Denayer','DEF','4'),
('Dubois','DEF','4'),
('Lopes','G','4'),

('David','ATT','5'),
('Yilmaz','ATT','5'),
('Bamba','ATT','5'),
('Ikone','ATT','5'),
('Sanches','MIL','5'),
('Andre','MIL','5'),
('Celik','DEF','5'),
('Fonte','DEF','5'),
('Botman','DEF','5'),
('Bradaric','DEF','5'),
('Maignan','G','5'),

('Guirassy','ATT','6'),
('Terrier','ATT','6'),
('Castillo','ATT','6'),
('Camavinga','MIL','6'),
('Bourigeaud','MIL','6'),
('Nzonzi','MIL','6'),
('Traoré','DEF','6'),
('Silva','DEF','6'),
('Aguerd','DEF','6'),
('Maoussa','DEF','6'),
('Gomis','G','6'),

('Dolberg','ATT','7'),
('Gouiri','ATT','7'),
('Lopes','ATT','7'),
('Lees-Melou','MIL','7'),
('Schneiderlin','MIL','7'),
('Kamari','DEF','7'),
('Nsoki','DEF','7'),
('Danilo','DEF','7'),
('Bambu','DEF','7'),
('Atal','DEF','7'),
('Benitez','G','7'),

('Delort','ATT','8'),
('Laborde','ATT','8'),
('Mavididi','ATT','8'),
('Mollet','MIL','8'),
('Ferri','MIL','8'),
('Savanier','MIL','8'),
('Sambia','DEF','8'),
('Mendes','DEF','8'),
('Congré','DEF','8'),
('Ristic','DEF','8'),
('Omlin','G','8'),

('Ajorque','ATT','9'),
('Diallo','ATT','9'),
('Thomasson','MIL','9'),
('Bellegarde','MIL','9'),
('Aholou','MIL','9'),
('Djiku','MIL','9'),
('Caci','DEF','9'),
('Mitrovic','DEF','9'),
('Simakan','DEF','9'),
('Lala','DEF','9'),
('Kamara','G','9'),

('Bahoken','ATT','10'),
('Boufal','ATT','10'),
('Cabot','ATT','10'),
('Fulgini','MIL','10'),
('Mangani','MIL','10'),
('Amadou','MIL','10'),
('Bamba','DEF','10'),
('Traoré','DEF','10'),
('Thomas','DEF','10'),
('Doumbia','DEF','10'),
('Bernardoni','G','10'),

('Maja','ATT','11'),
('Préville','ATT','11'),
('Arfa','ATT','11'),
('Oudin','ATT','11'),
('Basic','MIL','11'),
('Otavio','MIL','11'),
('Benito','DEF','11'),
('Koscielny','DEF','11'),
('Pablo','DEF','11'),
('Sabaly','DEF','11'),
('Costil','G','11'),

('Muani','ATT','12'),
('Simon','ATT','12'),
('Coco','ATT','12'),
('Blas','MIL','12'),
('Louza','MIL','12'),
('Chirivella','MIL','12'),
('Corchia','DEF','12'),
('Girotto','DEF','12'),
('Pallois','DEF','12'),
('Fabio','DEF','12'),
('Lafont','G','12'),
 
('Hamouma','ATT','13'),
('Bouanga','ATT','13'),
('Nordin','ATT','13'),
('Aouchiche','MIL','13'),
('Neyou','MIL','13'),
('Camara','MIL','13'),
('Debuchy','DEF','13'),
('Moukoudi','DEF','13'),
('Retsos','DEF','13'),
('Kolodziejczak','DEF','13'),
('Moulin','G','13'),

('Dia','ATT','14'),
('Sierhuis','ATT','14'),
('Cafaro','ATT','14'),
('Mbuku','ATT','14'),
('Berisha','MIL','14'),
('Munetsi','MIL','14'),
('Foket','DEF','14'),
('Faes','DEF','14'),
('Abdelhamid','DEF','14'),
('Konan','DEF','14'),
('Rajkovic','G','14'),
 
('Dia','ATT','14'),
('Sierhuis','ATT','14'),
('Cafaro','ATT','14'),
('Mbuku','ATT','14'),
('Berisha','MIL','14'),
('Munetsi','MIL','14'),
('Foket','DEF','14'),
('Faes','DEF','14'),
('Abdelhamid','DEF','14'),
('Konan','DEF','14'),
('Rajkovic','G','14'),
 
('Ganago','ATT','15'),
('Sotoca','ATT','15'),
('Kakuta','MIL','15'),
('Cahuzac','MIL','15'),
('Fofana','MIL','15'),
('Sylla','MIL','15'),
('Medina','DEF','15'),
('Barlé','DEF','15'),
('Gradit','DEF','15'),
('Clauss','DEF','15'),
('Leca','G','15'),
 
('Ripart','ATT','16'),
('Eliasson','ATT','16'),
('Ferhat','ATT','16'),
('Fomba','MIL','16'),
('Deaux','MIL','16'),
('Cubas','MIL','16'),
('Meling','DEF','16'),
('Landre','DEF','16'),
('Briancon','DEF','16'),
('Burner','DEF','16'),
('Reynet','G','16'),  

('Iseka','ATT','17'),
('Nguette','ATT','17'),
('Boulaya','ATT','17'),
('Pajot','MIL','17'),
('Angban','MIL','17'),
('Maiga','MIL','17'),
('Udol','DEF','17'),
('Boye','DEF','17'),
('Bronn','DEF','17'),
('Centonze','DEF','17'),
('Oukidja','G','17'), 
 
('Mounié','ATT','18'),
('Faivre','ATT','18'),
('Philippoteaux','ATT','18'),
('Charbonnier','MIL','18'),
('Belkebla','MIL','18'),
('Battocchio','MIL','18'),
('Pierre-Gabriel','DEF','18'),
('Hérelle','DEF','18'),
('Duverne','DEF','18'),
('Perraud','DEF','18'),
('Larsonneur','G','18'),
 
('Konaté','ATT','19'),
('Ebimbe','ATT','19'),
('Baldé','ATT','19'),
('Chouiar','MIL','19'),
('Lautoa','MIL','19'),
('Ndong','MIL','19'),
('Chafik','DEF','19'),
('Manga','DEF','19'),
('Panzo','DEF','19'),
('Chala','DEF','19'),
('Allagbé','G','19'),
 
('Moffi','ATT','20'),
('Grbic','ATT','20'),
('Boisgard','MIL','20'),
('Chalobah','MIL','20'),
('Abergel','MIL','20'),
('Wissa','MIL','20'),
('Mendes','DEF','20'),
('Laporte','DEF','20'),
('Gravillon','DEF','20'),
('Morel','DEF','20'),
('Nardi','G','20');
 




