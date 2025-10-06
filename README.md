# 🗣️ Voice RAG Customer-Care Assistant

A **voice-enabled customer care assistant** that answers user questions by retrieving from a small FAQ knowledge base using a **Retrieval-Augmented Generation (RAG)** pipeline.  
Supports **speech-to-text**, **vector search**, **LLM answering**, **text-to-speech**, and **order status** stub intent.

🌐 **Live Demo:** 👉 [https://rag-voice-assistant-2.onrender.com/](https://rag-voice-assistant-2.onrender.com/)

---

## ✨ Features

- 🎤 **Voice I/O** — Browser mic → STT (speech-to-text) → RAG → TTS (spoken answer)
- 🧠 **RAG Pipeline**
    - Ingests Markdown FAQ docs into vector DB
    - Embeds via local or OpenAI models
    - Retrieves top relevant chunks
    - Passes to LLM for answer generation
- 📚 **Citations** — Responses include short source tags (e.g., `(returns policy, billing faq)`).
- 🧾 **Intents**
    - Returns → answered via FAQ docs
    - Order Status → handled via stub API (`/api/order-status`)
- 🌐 **Deployed** on [Render](https://render.com) with Dockerfile
- ⚡ **Fast startup** & supports auto-ingestion

---

## 🏗️ Tech Stack

- **Java 17** + **Spring Boot 3**
- RAG implemented from scratch (no LangChain)
- **Local embedding** by default (OpenAI optional)
- Vector search with cosine similarity
- Frontend: simple static HTML + Web Speech API for STT/TTS
- Containerized with **Docker**

---

## 🚀 Getting Started (Local)

### 1️⃣ Clone the repository

```bash
git clone https://github.com/annubelgaonkar/RAG-voice-Assistant.git
cd RAG-voice-Assistant
```
2️⃣ Set environment variables

Create a .env.local (optional) or set via shell:
```bash
# Local embedding and LLM
APP_EMBEDDINGS_PROVIDER=local
APP_LLM_PROVIDER=local
APP_AUTOINGEST=true
```
If using OpenAI, also set:  
```bash
OPENAI_API_KEY=sk-xxxx
OPENAI_BASE_URL=https://api.openai.com/v1
LLM_MODEL=gpt-4o-mini
EMBEDDING_MODEL=text-embedding-3-small
```

3️⃣ Run the app
```bash
./mvnw clean spring-boot:run
```
App runs at: http://localhost:8080

🌐 Deployment (Render)

- This app is deployed using Render with Dockerfile at the repo root.

- Build Command: (empty, uses Dockerfile)

- Start Command: (from Dockerfile)

- Environment Variables:

  -- APP_EMBEDDINGS_PROVIDER=local

  -- APP_LLM_PROVIDER=local

  -- APP_AUTOINGEST=true

Logs show Tomcat starting → app live.

Free tier sleeps after inactivity.

📥 FAQ Ingestion

To ingest FAQs into the vector store:
```bash
curl -X POST https://rag-voice-assistant-2.onrender.com/api/ingest

```
Expected:
```bash
{"message":"Ingested FAQs","vectorCount":3,"chunks":3}
```
(Or enable auto-ingest on startup using APP_AUTOINGEST=true.)

💬 API Examples

🔸 Ask a Question (PowerShell)
```bash
Invoke-RestMethod -Uri "https://rag-voice-assistant-2.onrender.com/api/ask" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"query":"What is your returns policy?"}'
```
🔸 Ask a Question (curl - Windows CMD)
```bash
curl -X POST "https://rag-voice-assistant-2.onrender.com/api/ask" -H "Content-Type: application/json" -d "{\"query\":\"What is your returns policy?\"}"
```
🔸 Order Status (stub)
```bash
Invoke-RestMethod -Uri "https://rag-voice-assistant-2.onrender.com/api/order-status?orderId=ORD123" -Method GET
```
🧪 Example Response
```bash
{
  "answer": "Based on our policy: Returns Policy\n- Items can be returned within 30 days of delivery with proof of purchase.\n- Refunds issued to original payment method within 5-7 business days. (returns policy, billing faq)",
  "latencyMs": 1
}
```

