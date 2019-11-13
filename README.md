<H5>
This is a poc on Spring  integration with redis, without xml configuration files.
</H5>

<H4>Prerequisite </H4>
No redis connection explicitly defined in this project, assuming there is a loca redis server running on port 6379
<br/>
To test the functionality postman or similar API testing tool  or utility like curl is required. 

<H4>Reference</H4>
main documentation is available here https://docs.spring.io/spring-integration/docs/2.1.0.M1/reference/html/redis.html

further read https://www.baeldung.com/spring-integration

<H4>How to test</H4>
Run this application

<br/>

for a batch event simulation test hit the following url
<code>
 GET /api/simulate HTTP/1.1
 Host: localhost:9001
 Content-Type: text/plain
 </code>
 <br/>
 for testing one single message hit the following url
 <code>
 POST /api/simulate HTTP/1.1
 Host: localhost:9001
 Content-Type: text/plain
 payload: this is a sample payload
 </code>
                                      
                                      
 
