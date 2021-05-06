# A streaming application which is based on the Cloudflow framework and uses Akka-Streams and Flink.

The Cloudflow framework lets you connect different streaming frameworks - i.e. Akka-Streams, Flink, Spark-Streaming - seamlessly. One separate stream is 
called a streamlet. To exchange data between streamlets, Cloudflow uses Kafka as a message queue. All of these can be easily deployed and scaled via Kubernetes.

## Run locally:
To run the application locally on your machine without Kubernetes, just simply write 
```bash
sbt runLocal
```
command.

## Application blueprint:
Below, Kafka topic is a block with square brackets like [topic-name] and everything else is a streamlet.
```
            ┌───────────────┐
            │transfer-reader│
            └───────┬───────┘
                    │
                    v
              ┌───────────┐
              │[transfers]│
              └─────┬─────┘
                    │
           ┌────────┘
           │
           v
   ┌───────────────┐ ┌─────────────────┐
   │payment-checker│ │account-generator│
   └──────────┬┬───┘ └────────────┬────┘
              ││                  │
              └┼──────────┐       │
               v          │       v
   ┌────────────────────┐ │ ┌──────────┐
   │[validated-payments]│ │ │[accounts]│
   └─────────┬──────────┘ │ └─────┬────┘
             │            │       │
             │     ┌──────┼───────┘
             │     │      │
             v     v      │
       ┌─────────────────┐│
       │payment-processor││
       └───────────┬─────┘│
                   │   ┌──┘
                   │   │
                   v   v
              ┌────────────┐
              │[validation]│
              └──────┬─────┘
                     │
                     v
             ┌──────────────┐
             │invalid-logger│
             └──────────────┘
```


### Troubleshooting:

These are the problems and errors that I was faced with. My solutions to them might be strange, awkward and not-the-best-ones but at the end of the day, they work :)
This section is mostly like a documentation for my future self.

1) If avro schemas cannot link to your code, make sure you generated them with
```bash
sbt avroScalaGenerateSpecific
```
command and marked 'scala-avro' directory as generated files ('Mark As Generated Root' option in Intellij).

2) Use Scala 2.12 if you have Flink or Spark streamlets.

3) Sometimes, your explicit dependencies may vary with Cloudflow implicit dependencies which can cause a bunch of low-key errors. Double check version compatibility
when you add a dependency.

4) Too modern version of SBT can also cause some anomalies (at least, on Windows 10). That's why I used SBT 1.3.10.











