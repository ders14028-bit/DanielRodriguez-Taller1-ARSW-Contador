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

## Analysis

### Important context: printing is the bottleneck

Both programs printed every single number to the console. This means **the real workload here is not counting — it's printing**. Writing 1,000,000 lines to the screen is slow regardless of how many threads you use, because the console can only show one line at a time. All threads end up waiting for their turn to print, which is why the times across different thread counts are much closer together than you'd expect if the task were purely math.

---

### When threads = 1

Both languages perform similarly at 1 thread — Java at **20.25 s**, Go at **32.26 s**. There is no parallelism here; one worker does everything from start to finish. Java is actually faster because the JVM optimizes the loop heavily after it warms up. Go has a small extra cost just from starting its internal engine.

---

### When threads match the number of CPU cores (~8–12)

This laptop has around 8–12 logical processors. At **10 threads**, Java hits **23.71 s** and Go hits **25.27 s** — both close to their best times. This is the sweet spot: each thread gets its own CPU core and they all work truly in parallel without fighting over resources. Beyond this point, adding more threads doesn't add more real parallelism — it just adds more coordination.

---

### When threads = number of numbers (1,000,000)

At **1,000,000 threads**, each thread is responsible for printing exactly **one number**. This is the worst possible setup:

- **Java collapses to 149.61 s** — creating 1,000,000 real OS workers for a single print each is extremely wasteful. Most of the time is spent just setting them up and tearing them down.
- **Go stays at 26.38 s** — Go handles this gracefully because its goroutines are cheap to create. Even with 1,000,000 of them, Go's internal engine queues them up and processes them without asking the OS to do the heavy lifting.

---

### Why times don't improve much beyond ~10 threads

Since every thread calls `println`, they all compete to write to the same console output. Even if 100 threads are running, only one can print at a time — the rest wait. This is called **I/O contention** and it puts a ceiling on how fast the program can go regardless of thread count. That's why Java's times hover around 22–28 s between 100 and 10,000 threads — the console is the real bottleneck, not the CPU.

---

### Why does Go spike at 50 goroutines?

Go shows an unusual **39.46 s at 50 goroutines**, which is slower than even 1 goroutine. This is likely a one-time anomaly — the laptop may have had a background process interfere during that specific run. It doesn't represent a real pattern, as the surrounding values (10 and 100 goroutines) are both normal.

---

## Why does this happen?

### Java

In Java, every thread is a real worker that the **operating system** has to create and manage directly. Think of it like hiring a new employee for every small task — each one needs their own desk, tools, and onboarding time. When you have 1,000,000 threads, the OS is spending so much effort just keeping track of all of them that almost no actual counting gets done.

### Go

In Go, goroutines are managed by Go itself, not the OS. Think of a small team of workers (one per CPU core) that Go assigns tasks to as needed — no matter how many tasks you throw at it, the team size stays the same and they work through the queue efficiently. That's why 1,000,000 goroutines takes almost the same time as 1,000.

### Summary

| Observation | Plain explanation |
| --- | --- |
| Both slow at 1 thread | No parallelism, one worker does everything |
| Best performance around 10 threads | Matches the number of CPU cores available |
| Times plateau between 100–10,000 threads | Console printing limits speed, not the CPU |
| Java collapses at 1M threads | Too many real OS workers to create and manage |
| Go stays flat at 1M goroutines | Go handles workers internally, not the OS |
| Go spike at 50 goroutines | Likely a one-time interference from the laptop |

---

## Conclusion

The biggest lesson from this exercise is that **the bottleneck was printing, not counting**. Because every thread wrote to the console, threads spent most of their time waiting for each other instead of working in parallel. This flattened the results and made both languages look more similar than they really are for pure computation.

The clearest difference shows up at extreme thread counts. Java breaks down at 1,000,000 threads because the OS cannot handle that many real workers. Go handles it without issue because it manages concurrency on its own.

**Optimal range: 1,000–10,000 threads** for this specific setup. If the printing were removed and the task were pure math, both languages would scale much better and the differences would be even more dramatic.