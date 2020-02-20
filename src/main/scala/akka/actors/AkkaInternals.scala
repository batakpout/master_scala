/**
 * Akka has a thread pool that it shares with actors,akka schedules threads for execution using Akka Thread Scheduler
 * e.g 100 threads for 1 million actors per GB heap
 * Sending a message (messages are enqueued in actors mailbox, its thread safe)
 * Processing a message(
 * a thread is scheduled to run this actor
 * messages are extracted from mailbox in order
 * the thread invokes the handler(Receive block) on each message
 *
 * Guarantees
 * only one thread operates on one actor at a time, {so actors are effectively single threaded}
 * so no locks/synchronization needed
 * thread may never release actor in middle of processing messages {so message processing is atomic}
 *
 * Akka offers,
 * 1.) Message delivery guarantee at most once delivery
 * 2.) for any sender receiver pair, message order is maintained.
 *
 * e.g
 * If Alice sends Bob, message A followed by B then:
 * from 1.) Bob will never receive duplicates of A, B
 * from 2.) Bob will always receive A before B (possibly with some others in between)
 * */