# Counter with Threads
## Author: Daniel Esteban Rodriguez Suarez

---

## Description

Program that counts from 1 to 1,000,000 splitting the workload evenly across threads, implemented in both **Java** and **Go** to compare how each language handles running multiple tasks at the same time.

---

## Results

| Threads | Java | Go |
| --- | --- | --- |
| 1,000,000 | 149.613 s | 26.377 s |
| 500,000 | 44.688 s | 27.264 s |
| 100,000 | 30.237 s | 27.100 s |
| 50,000 | 28.167 s | 25.855 s |
| 10,000 | 23.714 s | 25.268 s |
| 5,000 | 27.042 s | 25.958 s |
| 1,000 | 25.698 s | 27.965 s |
| 500 | 22.584 s | N/A |
| 100 | 22.400 s | 25.188 s |
| 50 | 24.359 s | 39.464 s |
| 10 | 31.063 s | 34.421 s |
| 5 | 23.569 s | 26.864 s |
| 1 | 20.252 s | 32.261 s |

![Performance chart](<WhatsApp Image 2026-06-02 at 8.19.14 PM.jpeg>)

---

## Why does this happen?

### Java

In Java, every thread is a real worker that the **operating system** has to create and manage directly. Think of it like hiring a new employee for every small task — each one needs their own desk, tools, and onboarding time. When you have 1,000,000 threads, the OS is spending so much effort just keeping track of all of them that almost no actual counting gets done. That's why it reaches 149 s at 1M threads.

Java is fastest at **1 thread** because there's nothing to coordinate — one worker, one job, no waiting.

### Go

In Go, threads are called **goroutines** and they work differently. Instead of asking the OS to create a real worker for each one, Go manages them internally by itself. Imagine a small team of workers (one per CPU core) that Go assigns tasks to as needed — no matter how many tasks you throw at it, the team size stays the same and they just work through the queue. That's why 1,000,000 goroutines in Go takes almost the same time as 1,000.

Go is a bit slower at very few goroutines (1–10) because it still needs a moment to set up its internal team before starting.

### Summary

| Observation | Plain explanation |
| --- | --- |
| Java fastest at 1 thread | One worker, nothing to coordinate |
| Java very slow at 1M threads | Too many workers for the OS to manage |
| Go stays fast from 1k to 1M | Go handles workers internally, not the OS |
| Go slightly slow at 1–10 | Setup time before the first task runs |

---

## Conclusion

**Optimal range: 1,000–10,000 threads** — both languages perform well here. Java works better when you use very few threads; Go handles large numbers of threads much more efficiently because it doesn't rely on the OS to manage them.