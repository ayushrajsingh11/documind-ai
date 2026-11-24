CREATE EXTENSION IF NOT EXISTS vector;
CREATE TABLE IF NOT EXISTS accounts (username varchar(80) PRIMARY KEY, password_hash text NOT NULL);
CREATE TABLE IF NOT EXISTS documents (id uuid PRIMARY KEY, owner varchar(80) NOT NULL REFERENCES accounts(username), filename text NOT NULL, created_at timestamptz DEFAULT now());
CREATE TABLE IF NOT EXISTS chunks (id bigserial PRIMARY KEY, document_id uuid NOT NULL REFERENCES documents(id) ON DELETE CASCADE, ordinal integer NOT NULL, body text NOT NULL, embedding vector(1536) NOT NULL, UNIQUE(document_id, ordinal));
CREATE INDEX IF NOT EXISTS documents_owner_idx ON documents(owner);
