    Copyright 2016 Dot Technologies. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


# Firebase Cloud Messaging in Dot

This app uses Firebase Cloud Messaging (FCM) to know when to synchronize
the data and the user's schedule. For an introduction to FCM,
see [https://firebase.google.com/docs/cloud-messaging/index](https://firebase.google.com/docs/cloud-messaging/index)

On startup, the application registers the device with the FCM server (see
the BaseActivity.registerFCMClient method). This registration sends two
pieces of data: the device's FCM token id and the user's FCM Key. The FCM token id is a core FCM concept, and essentially consists of a long number that
identifies the device for the purposes of sending FCM messages.  The FCM
key, however, is something we use only in this app and is not part of the
normal FCM protocol. More about this soon.

The server can send the client several different types of FCM messages.
When a FCM message arrives, it is processed by FCMIntentService.onMessage.
That method will take the appropriate action depending on the content of
the message. There are several message verbs we recognize:

Message verb | Description
------------ | -----------
test | Prints a message to logcat. Because debugging is fun.
announcement | To announce any new offer or functionality
notification | Shows a notification to the user, optionally with a dialog.  Use sparingly. It can be annoying. See the [Notification command syntax](#notification-command-syntax) section below for more information about the format.

## Notification command syntax

An example of the syntax for the payload in the 'notification' command is
given below.

    {
        "format": "1.0.00",
        "audience": "all",
        "expiry": "2016-07-14T19:40:00Z",
        "title": "Title Goes Here",
        "message": "Message goes here testing 1234 foo bar qux",
        "minVersion": "200",
        "maxVersion": "201",
        "url": "http://www.google.com",
        "dialogTitle": "Dialog Title",
        "dialogText": "Hello! This is a test dialog. Lorem ipsum dolor sit amet."
        "dialogYes" : "Definitely!",
        "dialogNo" : "NO way!"
    }

Field | Required | Description
----- | -------- | -----------
audience | yes | can be "all", "local" (in-person attendees only) or "remote" (remote attendees only)
expiry | yes | indicates when the message expires (if devices get it after that date, they ignore it)
title | yes | title to appear in the notification
message | yes | message to appear in the notification
url | yes | the URL to direct the user to when they click the notification
maxVersion and minVersion | no | allow you to filter what version of the app will receive the notification. Use integer version codes like 200, 201, etc. You can specify only one endpoint (min or max) or both. Both are interpreted as *inclusive*
dialogTitle, dialogText, dialogYes, dialogNo | no | If the dialog fields are present, a dialog will be shown when the notification is clicked. That dialog will have the specified title and message. The message can contain newlines (use actual newlines, not \n), and will automaticaly linkify links. dialogYes can be ommitted for dialogs that do not have a positive action. For example "Dismiss" button only. The positive action (the YES button) will always launch the URL.


## FCM key

Here is some background around the problem we were trying to solve, so
that you can better understand our design decisions. First of all, there
has to be a data store that the app can access. Furthermore, the website needs to notify the application that the user's data has changed, triggering the application to do a sync that will fetch the fresh data. Another factor is that the user might have more than one Android device, so when they make a modification to their schedule on the website, the data should immediately update on ALL of their devices.

So these are the problems we had to solve:

1. how to protect that data so that only the user could access it
2. how to notify all of the user's devices when a change happens on their schedule
3. how to ensure users can't abuse the system sending FCM messages to devices that are not theirs

Items 1) are solved in a very elegant fashion: we simply use the
[Firebase Security Rules](https://firebase.google.com/docs/database/security/).
User data always stays in the user's own account, and this solves
the problem of access control to that data.

To tackle 2), one easy way would be to store the user's UID on the
server, associated with the device IDs, so that we knew, given a UID, what are all the devices that the user has and thus can send a message to all of them. The website, on the other hand, knows the UID of the user who is logged in, so they can instruct the FCM server to notify those user's devices when the schedule changes.

However, we wanted to minimize the amount of user information stored on
the server (there is no such thing as too much security!), and by using the
UID as key we would need an API on the FCM server that could send
a FCM message to all devices belonging to that user. But since someone's
UID is not a secret, a malicious user could, knowing a user's
UID, send an unending stream of FCM messages and drain their battery.
Clearly that would not be satisfactory security, and fails to solve item 3).

So, to solve 3), instead of using  UIDs we use a randomly generated
UUID which we call FCM key (aka FCM group ID). Each user has their own
unique FCM key, but a user can't guess another user's FCM key, and it's
impossible to determine to what particular user a FCM key belongs. So the
FCM server API takes FCM keys and not UIDs.

And where is the FCM key for a user stored? Easy: in their Firebase user data node, along with their all other attributes. That way both the app
and the website know the user's FCM key, and can use that to signal the
user's devices.

So if you look at BaseActivity.registerGCMClient, you will see how this
mechanism works: we first generate a random key locally; the first time we
try to sync with Firebase, we determine if there's already a key
there. If there is, the existing key overrides the local key.

Note that the app also sends a sync_user message to the server when the
user changes their schedule! This is done to cause a sync on all other
user's devices, so they can reflect that latest change as soon as possible.

## How to send a FCM message

If you've set up your FCM server and enabled FCM on Dot, you should
be able to push FCM messages to all your users. To do so, you have
to make an HTTP POST request to your FCM server (that is, your
App Engine app that's running the [FCM-server](../FCM-server) code).

Here is a sample POST request:

    POST /fcm/send/ HTTP/1.1
    Host://fcm.googleapis.com
    Authorization: key=$ADMINKEY
    Content-Type: application/json
    Content-Length: 22
    { "data": {
    "score": "5x1",
    "time": "15:10"
    },
    "to" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...
    }


This would send the "data" command to all users.
(replace $ADMINKEY by the admin key you configured in the FCM
server's)

To make this POST request, you can use one of the many command-line
HTTP utilities such as curl, or use openssl directly.

