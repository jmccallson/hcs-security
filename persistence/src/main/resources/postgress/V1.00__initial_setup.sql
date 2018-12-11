-- psql -h localhost -p 5432 -U postgres -W

CREATE TABLE sec_user_account
 (
     user_id character varying(20) NOT NULL,
     last_login_date timestamp without time zone,
     password character varying(130) COLLATE pg_catalog."default" NOT NULL,
     failed_login_count bigint,
     deleted_date timestamp without time zone,
     CONSTRAINT pk_user_account PRIMARY KEY (user_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE sec_program_key
(
    program_key_id  bigint NOT NULL,
    program_key character varying(50) COLLATE pg_catalog."default" NOT NULL,
    company_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    product_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    enabled boolean DEFAULT 'F'::boolean NOT NULL,
    user_agent character varying(100) COLLATE pg_catalog."default",
    owner_name character varying(100) COLLATE pg_catalog."default",
    partner_type bigint,
    refresh_token boolean DEFAULT 'F'::boolean,
    auto_create_account boolean DEFAULT 'F'::boolean,
    hmac_secret character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT pk_sec_dev_key PRIMARY KEY (program_key),
    CONSTRAINT idx_sec_dev_key_id UNIQUE (program_key_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE SEQUENCE SEQ_SEC_PROG_KEY START WITH 1000;

CREATE TABLE sec_program_key_oauth_callback
(
    program_key_id bigint NOT NULL,
    name character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    oauth_callback character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT sec_prog_key_oauth_callback_pk PRIMARY KEY (program_key_id, name, oauth_callback),
    CONSTRAINT prog_key_id_oauth_callback_fk FOREIGN KEY (program_key_id)
        REFERENCES sec_program_key (program_key_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE sec_realm
(
    realm_id bigint NOT NULL,
    realm_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    realm_uri character varying(200) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_sec_opd2_realm PRIMARY KEY (realm_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE SEQUENCE SEQ_PROG_REALM START WITH 1000;

-- Index: sec_opid2_realm_nm_idx

-- DROP INDEX sec_opid2_realm_nm_idx;

CREATE UNIQUE INDEX sec_opid2_realm_nm_idx
    ON sec_realm USING btree
    (realm_name COLLATE pg_catalog."default")
    TABLESPACE pg_default;

-- Index: sec_opid2_realm_uri_idx

-- DROP INDEX sec_opid2_realm_uri_idx;

CREATE UNIQUE INDEX sec_opid2_realm_uri_idx
    ON sec_realm USING btree
    (realm_uri COLLATE pg_catalog."default")
    TABLESPACE pg_default;

-- Index: sec_opid2_uid_rid_idx

-- DROP INDEX sec_opid2_uid_rid_idx;

CREATE UNIQUE INDEX sec_opid2_uid_rid_idx
    ON sec_realm USING btree
    (realm_id, realm_name COLLATE pg_catalog."default")
TABLESPACE pg_default;

CREATE TABLE sec_prog_key_openid_realm
(
    program_key_id bigint NOT NULL,
    realm_id bigint NOT NULL,
    CONSTRAINT pk_dor_prog_key_realm PRIMARY KEY (program_key_id, realm_id),
    CONSTRAINT sec_dor_prog_key_id_fk FOREIGN KEY (program_key_id)
        REFERENCES sec_program_key (program_key_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT sec_dor_realm_id_fk FOREIGN KEY (realm_id)
        REFERENCES sec_realm (realm_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE sec_openid_connect_provider
(
    op_id bigint NOT NULL,
    op_name character varying(65) COLLATE pg_catalog."default" NOT NULL,
    op_description character varying(200) COLLATE pg_catalog."default",
    op_discovery_endpoint character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    authorization_endpoint character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    token_endpoint character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    user_info_endpoint character varying(1024) COLLATE pg_catalog."default",
    certificate character varying(4000) COLLATE pg_catalog."default",
    client_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    op_image character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    op_assurance_level character varying(16) COLLATE pg_catalog."default" DEFAULT '1.1'::character varying,
    op_provider_realm character varying(1024) COLLATE pg_catalog."default",
    op_issuer character varying(1024) COLLATE pg_catalog."default",
    jwt_assrtn_client_id character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT pk_sec_openid_connect_provider PRIMARY KEY (op_id),
    CONSTRAINT idx_sec_ocp_issuer UNIQUE (op_issuer),
    CONSTRAINT idx_sec_ocp_name UNIQUE (op_name)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE SEQUENCE SEQ_SEC_CONNECT_PROVIDER START WITH 1000;

CREATE TABLE sec_prog_key_sec_provider
(
    program_key_id bigint NOT NULL,
    op_id bigint NOT NULL,
    CONSTRAINT prog_key_sec_provider_pk PRIMARY KEY (program_key_id, op_id),
    CONSTRAINT prog_key_opc_dev_key_fk FOREIGN KEY (program_key_id)
        REFERENCES sec_program_key (program_key_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT prog_key_opc_openid_prov_fk FOREIGN KEY (op_id)
        REFERENCES sec_openid_connect_provider (op_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE sec_connect_identity_link
(
    subject character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    user_id character varying(20) NOT NULL,
    sec_prov_id bigint NOT NULL,
    CONSTRAINT pk_sec_identity_link PRIMARY KEY (subject, sec_prov_id),
    CONSTRAINT sec_link_fk FOREIGN KEY (user_id)
        REFERENCES sec_user_account (user_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT sec_prov_id_link_fk FOREIGN KEY (sec_prov_id)
        REFERENCES sec_openid_connect_provider (op_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE sec_oauth2_auth_code
(
    authorization_id bigint NOT NULL,
    user_id character varying(20) NOT NULL,
    expiration_date timestamp without time zone NOT NULL,
    client_id bigint NOT NULL,
    session_id character varying(200) COLLATE pg_catalog."default" NOT NULL,
    authorization_code character varying(200) COLLATE pg_catalog."default" NOT NULL,
    refresh_token character varying(200) COLLATE pg_catalog."default",
    authorization_code_used boolean NOT NULL,
    refresh_expiration_date timestamp without time zone,
    max_refresh_expiration_date timestamp without time zone NOT NULL,
    authorization_redirect_uri character varying(1024) COLLATE pg_catalog."default",
    private boolean NOT NULL,
    openid_connect boolean DEFAULT  'F'::boolean,
    scope_request character varying(1024) COLLATE pg_catalog."default",
    CONSTRAINT pk_sec_oauth2_auth_code PRIMARY KEY (authorization_code),
    CONSTRAINT sec_authorization_id_idx UNIQUE (authorization_id),
    CONSTRAINT sec_oacs_user_id_fk FOREIGN KEY (user_id)
        REFERENCES sec_user_account (user_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT sec_oacs_client_id_fk FOREIGN KEY (client_id)
        REFERENCES sec_program_key (program_key_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT chk_authorization_code_used CHECK (authorization_code_used::boolean = ANY (ARRAY['T'::boolean::boolean, 'F'::boolean::boolean])),
    CONSTRAINT chk_openid_connect CHECK (openid_connect::boolean = ANY (ARRAY['T'::boolean::boolean, 'F'::boolean::boolean])),
    CONSTRAINT chk_private CHECK (private::boolean = ANY (ARRAY['T'::boolean::boolean, 'F'::boolean::boolean]))
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE SEQUENCE SEQ_SEC_AUTHORIZATION_ID START WITH 1000;

-- DROP INDEX sec_oauth2_user_id_idx;

CREATE INDEX sec_oauth2_user_id_idx
    ON sec_oauth2_auth_code USING btree
    (user_id)
    TABLESPACE pg_default;

-- Index: sec_oauth2_client_id_idx

-- DROP INDEX sec_oauth2_client_id_idx;

CREATE INDEX sec_oauth2_client_id_idx
    ON sec_oauth2_auth_code USING btree
    (client_id)
    TABLESPACE pg_default;

-- Index: sec_oauth2_expiration_date_idx

-- DROP INDEX sec_oauth2_expiration_date_idx;

CREATE INDEX sec_oauth2_expiration_date_idx
    ON sec_oauth2_auth_code USING btree
    (expiration_date)
    TABLESPACE pg_default;

-- Index: sec_oauth2_refresh_token_idx

-- DROP INDEX sec_oauth2_refresh_token_idx;

CREATE INDEX sec_oauth2_refresh_token_idx
    ON sec_oauth2_auth_code USING btree
    (refresh_token COLLATE pg_catalog."default")
    TABLESPACE pg_default;

-- Index: sec_oauth2_session_idx

-- DROP INDEX sec_oauth2_session_idx;

CREATE UNIQUE INDEX sec_oauth2_session_idx
    ON sec_oauth2_auth_code USING btree
    (session_id COLLATE pg_catalog."default")
    TABLESPACE pg_default;

-- Index: sec_oauth_max_refresh_exp_date

-- DROP INDEX sec_oauth_max_refresh_exp_date;

CREATE INDEX sec_oauth_max_refresh_exp_date
    ON sec_oauth2_auth_code USING btree
    (max_refresh_expiration_date)
    TABLESPACE pg_default;

-- Index: sec_oauth_refresh_exp_date

-- DROP INDEX sec_oauth_refresh_exp_date;

CREATE INDEX sec_oauth_refresh_exp_date
    ON sec_oauth2_auth_code USING btree
    (refresh_expiration_date)
TABLESPACE pg_default;

CREATE TABLE sec_oauth2_friend
(
    program_key_id bigint NOT NULL,
    friend_prog_key_id bigint NOT NULL,
    CONSTRAINT sec_oauth2_friend_pk PRIMARY KEY (program_key_id, friend_prog_key_id),
    CONSTRAINT friend_prog_key_id_fk FOREIGN KEY (program_key_id)
        REFERENCES sec_program_key (program_key_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT friend_friend_prog_key_id_fk FOREIGN KEY (friend_prog_key_id)
        REFERENCES sec_program_key (program_key_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE sec_openid2_user_realm_mapping
(
    user_id character varying(20) NOT NULL,
    realm_id bigint NOT NULL,
    create_date date NOT NULL DEFAULT ('now'::text)::date,
    CONSTRAINT pk_sec_openid2_user_realm PRIMARY KEY (user_id, realm_id),
    CONSTRAINT sec_opid2_urm_as_fk FOREIGN KEY (user_id)
        REFERENCES sec_user_account (user_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT sec_opid2_urm_rs_fk FOREIGN KEY (realm_id)
        REFERENCES sec_realm (realm_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


