/*
 * Swift Parallel Scripting Language (http://swift-lang.org)
 *
 * Copyright 2012-2014 University of Chicago
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/*
 * coaster-client.h
 *
 *  Created on: Jun 9, 2012
 *      Author: mike
 */

#ifndef COASTER_CLIENT_H_
#define COASTER_CLIENT_H_

#ifndef SWIG

#include <string>
#include "Lock.h"
#include "ConditionVariable.h"
#include "Command.h"
#include "CoasterChannel.h"
#include "CoasterLoop.h"
#include "HandlerFactory.h"
#include "Job.h"
#include "Settings.h"
#include <list>
#include <map>

#include <netdb.h>

#endif 

namespace Coaster {

class ClientHandlerFactory;
class HandlerFactory;
class CoasterLoop;
class CoasterChannel;

class CoasterClient: public CommandCallback {
	private:
		Lock lock;
		ConditionVariable cv;
		std::string URL;
		std::string* hostName;
		CoasterChannel* channel;
		bool started;

		int sockFD;

		int getPort();
		const std::string& getHostName();
		struct addrinfo* resolve(const char* hostName, int port);

		CoasterLoop* loop;
		HandlerFactory* handlerFactory;

		std::map<coaster_job_id, Job*> jobs;

		std::list<Job*> doneJobs;
		
                /* Disable default copy constructor */
		CoasterClient(const CoasterClient&);
		/* Disable default assignment */
		CoasterClient& operator=(const CoasterClient&);
	public:
		CoasterClient(std::string URL, CoasterLoop& loop);
		virtual ~CoasterClient();
		void start();
		void stop();
		
		// TODO: how long does this hold a reference to settings?
		std::string setOptions(Settings& settings);

		/*
		 * Submit a job.  The job should have been filled in with
		 * all properties.  The ownership of the job object stays
		 * with the caller, but this client will retain a reference
		 * to the job until done jobs are purged.
                 *
                 * configId: service-side config to use
		 */
		void submit(Job& job, const std::string& configId);

		/*
		 * Wait for job to be done.  Upon completion no actions
		 * are taken: job must be purged from client explicitly.
		 */
		void waitForJob(const Job& job);

		/*
		 * Return a list of done jobs and remove references from this
		 * client.  This returns jobs in FIFO order of completion
		 */
		std::list<Job*>* getAndPurgeDoneJobs();
		
		/*
		 * Give back up to size done jobs and remove references.
		 * jobs: array with space for at least size jobs
		 * returns number of completed jobs added to array
		 */
		int getAndPurgeDoneJobs(int size, Job** jobs);

		void waitForJobs();
		/*
		 * Wait until there is at least one done job
		 */
		void waitForAnyJob();

		void updateJobStatus(const std::string& remoteJobId, JobStatus* status);
		void updateJobStatus(coaster_job_id jobId, JobStatus* status);
		void updateJobStatus(Job* job, JobStatus* status);

		const std::string& getURL();

		void errorReceived(Command* cmd, std::string* message, RemoteCoasterException* details);
		void replyReceived(Command* cmd);
	private:
		void updateJobStatusNoLock(Job* job, JobStatus* status);
};

}

#endif /* COASTER_CLIENT_H_ */
