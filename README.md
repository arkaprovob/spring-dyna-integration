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
1. Run this application
<br/>
2. Go to redis cli create a new queue with this pattern <code>*Queue</code>
for example <code>Jadis-Queue</code> then push message into the queue  <code> LPUSH Wadis-Queue 'HELLO Spring Integration'</code>
you can create as many keys as you want with the mentioned pattern and push message into it.
<br/>
3. Hit this URL <code>http://localhost:9001/refresh</code>
 
                                      
                                      
 
