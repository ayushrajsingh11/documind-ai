# Architecture decisions

## Scope
Searches lines of supplied document text for a case-insensitive keyword.

## ADR 001 — Modular monolith
Use one Spring Boot process and a React client until workload or team boundaries justify service extraction. Keep domain logic independent from HTTP to simplify tests.

## ADR 002 — Deterministic baseline
The initial workflow requires no external model or credentials. Future AI outputs must be validated, bounded and evaluated against this baseline.

## Data and security
No persistence or authentication in v0.1. Limit input lengths and return generic validation errors. Public hosting requires rate limits, authentication and resource limits.

## Next milestones
PDF parsing, embeddings, RAG, citations and tenant isolation.
