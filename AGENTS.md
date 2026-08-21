# Project Overview

청년 정부 지원정책을 수집·아카이빙하고,
사용자가 자신의 조건에 맞는 정책을 검색할 수 있도록 하며,
추후 RAG 기반으로 공식 원문 근거가 포함된 답변을 제공하는 서비스이다.

## Team

- 1인 개발 프로젝트이다.
- Codex를 개발 보조 에이전트로 적극 활용한다.
- MVP 완성을 최우선으로 한다.
- 개발자가 이해하고 설명할 수 있는 수준의 구조를 유지한다.

## Planned Architecture

현재부터 모든 기술을 한 번에 도입하지 않는다.
아래 기술은 프로젝트의 최종 계획이며 필요한 단계에 도달했을 때 추가한다.

- Frontend: Next.js + TypeScript
- Backend: Java 17 + Spring Boot
- Database: PostgreSQL
- Vector Search: pgvector (RAG 단계에서 도입)
- AI Server: Python + FastAPI (문서 및 RAG 단계에서 도입)
- Document Processing: Docling (문서 처리 단계에서 도입)
- Agent: LangGraph (마지막 확장 단계에서 도입)

## Development Principles

- 한 번에 하나의 작은 기능만 구현한다.
- 동작하는 가장 단순한 구조를 우선한다.
- 미래 확장성을 이유로 현재 필요하지 않은 구조를 만들지 않는다.
- 기존 코드를 임의로 대규모 수정하지 않는다.
- 새로운 라이브러리나 인프라를 임의로 추가하지 않는다.
- 새로운 기술이 필요하다면 구현 전에 필요한 이유와 대안을 설명한다.
- Controller, Service, Repository 역할을 분리한다.
- Entity를 API 응답으로 직접 반환하지 않는다.
- DTO를 사용한다.
- 가능한 경우 새 기능에 테스트를 작성한다.
- 작업 후 빌드와 테스트를 실행한다.
- 테스트가 실패하면 원인을 확인하고 수정한다.
- 개발자가 이해하고 설명하기 어려운 구조를 불필요하게 도입하지 않는다.

## Codex Working Rules

- 사용자가 요청한 작업 범위 밖의 기능을 임의로 구현하지 않는다.
- 작업 요청을 받으면 가능하면 구현 전에 변경 계획을 먼저 설명한다.
- 불필요한 파일이나 의존성을 추가하지 않는다.
- 기존 코드 구조를 변경해야 한다면 먼저 이유를 설명한다.
- 작업 완료 후 변경된 파일을 설명한다.
- 작업 완료 후 관련 테스트와 빌드를 실행한다.
- 테스트 또는 빌드 실패를 숨기지 않는다.
- 사용자가 명시적으로 요청하지 않은 대규모 리팩터링을 하지 않는다.

## Development Roadmap

개발 순서는 다음을 기본으로 한다.

1. Spring Boot 프로젝트 초기화
2. PostgreSQL 연결
3. Spring Data JPA 설정
4. Policy 도메인 구현
5. Policy CRUD
6. 조건별 정책 검색
7. 공식 Open API 연동
8. Frontend
9. PDF 문서 관리
10. FastAPI + Docling
11. pgvector + Embedding
12. RAG
13. 정책 자격조건 진단
14. 정책 버전 아카이빙
15. Multimodal
16. LangGraph Agent

앞 단계가 충분히 동작하기 전에 뒷 단계 기술을 미리 구현하지 않는다.

## Current Phase

현재는 Backend MVP의 초기 구축 단계이다.

현재 목표:

1. 실행 가능한 Spring Boot 프로젝트를 만든다.
2. 기본 빌드와 테스트가 정상적으로 동작하도록 한다.
3. 이후 PostgreSQL 및 Policy 도메인을 단계적으로 추가한다.

## Do Not Implement Yet

현재 단계에서는 다음 기능이나 기술을 구현하지 않는다.

- Frontend
- Authentication
- Spring Security
- JWT
- Crawling
- Spring Batch
- FastAPI
- Docling
- RAG
- Embedding
- pgvector
- Vector Search
- Multimodal
- LangGraph
- Redis
- Kafka
- Elasticsearch
- Kubernetes
- Microservices

## Decision Rule

새로운 기술을 추가하기 전 다음 질문에 답할 수 있어야 한다.

1. 현재 어떤 문제가 있는가?
2. 이 기술이 그 문제를 해결하는가?
3. 더 단순한 방법으로 해결할 수 없는가?
4. 지금 반드시 필요한가?

답이 명확하지 않다면 해당 기술을 도입하지 않는다.