# micronaut-jdbc-example
Small example how to use JDBC transactions in Micronaut

Getting JDBC into Micronaut is not that difficult, but I couldn't get the `@Transactional` from
micronaut-spring to work. It seems to work when using Hibernate, as this installs it's own
PlatformTransactionManager. With plain JDBC, no transaction manager is in place. I decided
to write a small function, which just starts the transaction and commits it when no exception
is thrown.

When trying to build something with @Transaction, I get funny exceptions:

```
io.micronaut.runtime.Micronaut - Error starting Micronaut server: Cannot inherit from final class
java.lang.VerifyError: Cannot inherit from final class
```

## Running

`./gradlew run`

## Testing

Working version, manual transaction handling: `curl http://localhost:8080/manual` - the counter increases with every request

Non-working version, @Transactional transaction handling: `curl http://localhost:8080/transactional` - the counter doesn't increase, which means the transaction doesn't get committed
