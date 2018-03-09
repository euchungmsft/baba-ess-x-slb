# baba-ess-x-slb
CMS API example which implements trigger firing Autoscaling Event by failures from SLB

Alibaba Autoscaling (ESS) currently supports number of indicators which incurs autoscaling events such as availability of instance, CPU utilization, bandwidth and so on. But sometimes, for example, in case the application does not perfectly handle the peak loads, traffics and hang, adding a brand new instance could be helpful instead of time-taking troubleshooting and problem solving at the issues. Because of this, some of our competitors offers SLB status as one of the autoscaling indicator. This example implements this by using Alibaba API

Requirement 

1. JDK 1.8
2. Eclipse 
3. Gradle

How to install

1. Pull the repository
2. Import the project to workspace
3. Refresh Gradle project
4. Copy sample.clientprofile to .clienprofile and make some changes
5. Run

Configuration

in the .clientprofile

1. key.id=<your account key>
2. key.secret=<your account key secret>
3. endpoint=<API endpoint, ex) cn-shanghai>

4. stop-on-failure=<stops the instance when SLB status is 'abnormal', true | false>

5. region-id=<region where your SLB's running, ex) cn-shanghai>
6. slb-id=<the SLB's id>
7. acrion-ari=<the action ARI to run when the failure's found from SLB. it has to be defined in your the autoscaling configuration>

8. webhook-url=<the webhook URL for subscription mode. remark this when it's run by polling, ex) tcp://localhost:61616>

5-6-7 are optional when stop-on-failure is true

Notes

1 Polling mode
Simply run this script from commandline or from cron. There's no need to change the code. Be careful with number of intervals, intervals by SLB's healthchecks, mutes from Cloud Monitoring Service (CMS) Alert, CMS's own delays and as well as the intervals defiend from your crntab

2 Subscription mode
The CMS offers webhook integrations to send its alerts. It sends the alerts to somewhere else which gets POSTs in JSON format.
See https://github.com/nudbeach/baba-cms-webhook for further details. 

Try to add your codes under "//	do something" on Main.java. This example does not contain when the messages comes from webhook


