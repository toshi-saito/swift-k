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


#include "Handler.h"

#include <cstring>

#include "ChannelCallback.h"
#include "Logger.h"

using namespace Coaster;

using std::list;
using std::string;
using std::vector;

void Handler::receiveCompleted(int flags) {
	if (flags & FLAG_ERROR) {
		errorReceived();
	}
	else {
		requestReceived();
	}
}

void Handler::errorReceived() {
	vector<Buffer*>* errorData = getErrorData();
	if (errorData == NULL) {
		LogWarn << "Unspecified error receiving request." << endl;
	}
	else if (errorData->size() == 1) {
		string msg;
		errorData->at(0)->str(msg);
		LogWarn << "Error receiving request: " << msg << endl;
	}
	else {
		string msg, detail;
		errorData->at(0)->str(msg);
		errorData->at(1)->str(detail);
		LogWarn << "Error receiving request: " << msg << "\n" << detail << endl;
	}
}

void Handler::requestReceived() {
	sendReply("OK");
}

void Handler::sendReply(string& msg) {
	addOutData(Buffer::wrap(msg));
	send(getChannel());
}

void Handler::sendReply(const char* msg) {
	addOutData(Buffer::wrap(msg));
	send(getChannel());
}

void Handler::send(CoasterChannel* channel) {
	list<Buffer*>* od = getOutData();

	while (od->size() > 0) {
		Buffer* b = od->front();
		// od->size() is 1 on the last chunk, since the removal happens
		// at the end of the loop
		int flags = FLAG_REPLY + (od->size() == 1 ? FLAG_FINAL : 0);
		channel->send(tag, b, flags, &DeleteBufferCallback::CALLBACK);
		od->pop_front();
	}
}
