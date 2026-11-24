# DocuMind AI

A Java 21 / Spring Boot / React document intelligence workspace with real Spring AI integrations and PostgreSQL vector retrieval.

## v0.2 implemented source
- Account registration, BCrypt passwords, session authentication and CSRF protection.
- PDF, DOCX, XLSX, TXT and CSV extraction (text documents only; no OCR).
- Overlapping text chunks, OpenAI embeddings via Spring AI, pgvector cosine ranking with owner-scoped SQL.
- Document upload, listing, deletion, optional single-document retrieval and chat with numbered evidence excerpts.
- Responsive three-panel React workspace and local Docker Compose deployment.

## Quick start
Requires Docker Compose and an OpenAI API key with embedding/chat access (usage may incur cost).

```bash
cp .env.example .env
# Edit .env: set DATABASE_PASSWORD and OPENAI_API_KEY
 docker compose up --build
``
Open http://localhost:3000, create a workspace account (12+ character password), upload a synthetic document and ask a question. Never commit .env. The OpenAI provider receives document chunks and question text; do not upload confidential information without authorization.

## Demo scenario
Use [sample policy](samples/refund-policy.txt). Ask “When does a refund require approval?” and inspect the retrieved passages. Upload a second file, select it in the library, and observe scoped retrieval. Create a second account to check that the first account's documents are not visible.

## Architecture
```mermaid
flowchart LR
 UI[React workspace] --> API[Spring Security + REST]
 API --> P[Document parser]
 P --> E[Spring AI embeddings]
 E --> DB[(Postgres + pgvector)]
 API --> R[Owner-scoped retrieval]
 R --> DB
 R --> L[Spring AI chat]
 L --> UI
``
See [architecture notes](docs/architecture.md).

## Verification
Run `mvn -f backend/pom.xml test` and `cd frontend && npm install && npm run build`. GitHub Actions is configured. This commit has not yet been build- or runtime-verified. Provider calls require valid credentials.

## Limitations and roadmap
Session auth rather than JWT; one library per account; synchronous ingestion and chat; no OCR, streaming, persistent chat history, citation verification, shared workspaces, public deployment or production security hardening. Source references are retrieved passages, not proof that the model's statements are correct. Add integration tests, quotas, rate limiting, parser sandboxing, model timeouts, retrieval evaluation, observability, and TLS before internet exposure. Local demo only.
