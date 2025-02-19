--  Description: This script is used to create the table and sequence for the player entity.
CREATE SEQUENCE IF NOT EXISTS player_id_seq;

-- Table: player
-- Description: This table is used to store the player information.
CREATE TABLE IF NOT EXISTS player (
  id BIGINT PRIMARY KEY DEFAULT nextval('player_id_seq'),
  last_name CHARACTER VARYING(50) NOT NULL,
  first_name CHARACTER VARYING(50) NOT NULL,
  birth_date DATE NOT NULL,
  points INTEGER NOT NULL,
  rank INTEGER NOT NULL
);

-- Sequence: player_id_seq
-- Description: This sequence is used to generate the id for the player table.
ALTER SEQUENCE player_id_seq OWNED BY player.id;

-- Owner: postgres
-- Description: This command is used to set the owner of the player table to the postgres user.
ALTER TABLE IF EXISTS public.player OWNER TO postgres;

INSERT INTO public.player(last_name, first_name, birth_date, points, rank)
	VALUES ('Nadal', 'Rafael', '1986-06-03', 5000, 1);

INSERT INTO public.player(last_name, first_name, birth_date, points, rank)
    	VALUES ('Djokovic', 'Novak', '1987-05-22', 4000, 2);

INSERT INTO public.player(last_name, first_name, birth_date, points, rank)
    	VALUES ('Federer', 'Roger', '1981-08-08', 3000, 3);

INSERT INTO public.player(last_name, first_name, birth_date, points, rank)
    	VALUES ('Murray', 'Andy', '1987-05-15', 2000, 4);
