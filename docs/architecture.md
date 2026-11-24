# DocuMind architecture

## Ingestion
Authenticate -> validate file extension and size -> parse -> split -> embed -> persist document and chunks in a single transaction. Provider calls occur before the transaction. The original binary is not retained. Chunk ordinal is zero-based; display adds one.

## Retrieval
Embed question, rank by pgvector cosine distance, filter by owner and optionally document UUID before taking six results. Pass source excerpts to Spring AI ChatClient. Numbered citations are prompted rather than enforced; users can inspect all returned excerpts. Exact search intentionally avoids approximate index recall and tenant-filter interactions for this small demo.

## Security
BCrypt password hashing; same-origin HttpOnly session cookie; CSRF tokens obtained from authenticated or anonymous endpoint; owner is derived from Principal, never accepted from client. Parameterized SQL. Cascade deletion. Documents are untrusted input; prompt injection mitigations are not a guarantee. OpenAI receives extracted text. Local HTTP only; secure cookies and TLS required in deployment.

## ADRs
1. Modular monolith to simplify deployment and transactions.
2. PostgreSQL with vector extension and explicit JDBC retrieval to enforce ownership predicates.
3. Session auth instead of browser-stored JWT, reducing token exposure to JavaScript.
4. Synchronous ingestion for the initial demo; future jobs need durable status and bounded concurrency.

## Validation roadmap
Cross-account access tests, malformed uploads, zip expansion limits, provider failures, restart persistence, CSRF, RAG evaluation, citation correctness and load tests. No claims of measured performance yet.
