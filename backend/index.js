import  express  from 'express'
import { Skyflow } from 'skyflow-node';
import { getRedactedNumber, shouldBlock } from './services';
import { skyflowClient } from './skyflow';
const app = express();
const port = 3000;

const routes = {
  VERIFY: "/verify",
}
app.use(express.json());
app.use(express.urlencoded({extended:false}));

const tablename = "table1"

app.get(routes.VERIFY, async (req, res) => {
  const {caller_id, receiver_id, isMyContact} = req.body

  try {
    const details = await skyflowClient.getById(
    {
      records: [{
          ids: [caller_id, receiver_id],
          table: tablename,
          redaction: Skyflow.RedactionType.PLAIN_TEXT
      }]
    })
    console.log(details["records"])
  const caller_details = details["records"][0]["fields"]
  const receiver_details = details["records"][0]["fields"]

  if(shouldBlock(receiver_details["config"]["blocked_calls"], caller_details)) {
    res.send({
      allow: false
    })
  } else {
    let redactedNumber = getRedactedNumber(caller_details, receiver_details, isMyContact)
    res.send({
      allow: true,
      caller_number: redactedNumber
    })
  }} catch(err){
    console.log('rejected')
    console.log(err)
  }
})

app.listen(port, () => {
  console.log(`skyCaller Service listening at http://localhost:${port}`);
});

