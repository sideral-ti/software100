spring.application.name=jpa

server.port=9000

# Postgres
spring.datasource.url=jdbc:postgresql://localhost:5432/jpa_test?createDatabaseIfNotExist=true
spring.datasource.username=postgres
spring.datasource.password=sideralti
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Logging
logging.level.org.springframework=INFO
logging.level.org.hibernate=INFO

