const catalyst = require('zcatalyst-sdk-node')
const fs = require('fs')
const path = require('path')
const { log } = require('console')

module.exports = (event, context) => {
  /* 
        EVENT FUNCTIONALITIES
    */
  // const DATA = event.data; //event data
  // const TIME = event.time; //event occured time

  // const SOURCE_DETAILS = event.getSourceDetails(); //event source details
  // const SOURCE_ACTION = SOURCE_DETAILS.action; //(insert | fetch | invoke ...)
  // const SOURCE_TYPE = SOURCE_DETAILS.type; //(datastore | cache | queue ...)
  // const SOURCE_ENTITY_ID = SOURCE_DETAILS.entityId; //if type is datastore then entity id is tableid

  // const SOURCE_BUS_DETAILS = SOURCE_DETAILS.getBusDetails(); //event bus details
  // const SOURCE_BUS_ID = SOURCE_BUS_DETAILS.id; //event bus id

  // const PROJECT_DETAILS = event.getProjectDetails(); //event project details
  // const FUNCTION_DETAILS = event.getFunctionDetails(); //event function details

  const app = catalyst.initialize(context)

  const EVENT_DETAILS = event.getSourceDetails()
  debugger
  let source = EVENT_DETAILS.type
  let action = EVENT_DETAILS.action
  if (source === 'UserManagement' && action === 'SignUp') {
	log("UserManagement or SignUp action")
    try {
      let eventData = event.data
      let userEmailId = eventData.user_details.email_id
      let email = app.email()
      let config = {
        from_email: 'kibetimmanuel0@gmail.com', // Replace this with the email you configured
        to_email: userEmailId,
        subject: 'We welcome you on board!',
        content: fs
          .readFileSync(path.join(__dirname, 'invite.html'))
          .toString(),
        html_mode: true,
      }
      let mailPromise = email.sendMail(config)
      mailPromise.then((mailObject) => {
        console.log(mailObject)
        context.closeWithSuccess()
      })
    } catch (err) {
      console.error(err)
      context.closeWithFailure()
    }
  } else {
    console.log('Not a Sign up Event')
    context.closeWithFailure()
  }
}
