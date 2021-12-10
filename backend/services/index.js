import axios from 'axios'
const token = 'Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2MiOiJmNzk0ZmY4NmZiYzgxMWVhYmQ2YzNhOTExNDNlM2Q0MiIsImF1ZCI6Imh0dHBzOi8vbWFuYWdlLnNreWZsb3dhcGlzLmRldiIsImV4cCI6MTYzOTIwMTU0MjQxNCwiaWF0IjoxNjM5MTE1MTQyNDE0LCJpc3MiOiJzYS1hdXRoQG1hbmFnZS5za3lmbG93YXBpcy5kZXYiLCJqdGkiOiJkYTkwYjg3ZTczZTY0YTljODdiMjc1NWFjZDZhMzMyNiIsInN1YiI6ImEyMWE0MDI3MmM5MjQ1YzQ4ODZjNDFlNjU4NDdjMTMxIn0.XWlUBIbNha6xPW-hCmKgK7e30mmdRGpxFirv6nJYJiW7FnAZ6-crVBIVDifTPG3OvZ0iqWDPKSo8-8cNOdPbmH3wa4umuHwj0dD4Dsugysp_eLRCVcSvcFacZ9j5bcLmPbsxMzDMLCPAGHFJ7Xp_0gKDt-9j-dMnDCMIg2mCMtA8AX4v5xqfr5DU1nNax9p8-FZdTs8PruPQLGfr4WlBZR631wLc0z2f7kvC-gTP7VCJ2k23zGatW34uVPIhMRqOdMyTUWTZFaCnWWNVc3u_j5BvPkbVTeN_rjP0uslvbZ2YaUjs3XvsJDLX9NfVOAEKItPUGBaY9GQaEMQzKBYIIQ'

export const shouldBlock= (receiver_rules, caller_details) => {
    const blockedCountries = getCountrySet(receiver_rules["blocked_countries"])
    const blockBusiness = receiver_rules["block_business"]
    const blockSpam = receiver_rules["block_spam"]
    
    const callerCountry = caller_details["country"]

    const isCallerBusiness = caller_details["is_business"]
    const isCallerSpam = caller_details["is_spam"]


    return (blockedCountries.has(callerCountry) || (blockBusiness && isCallerBusiness) && (blockSpam && isCallerSpam))
}

const getCountrySet = (countriesStr) =>  {
    if(countriesStr) {
        return new Set(countriesStr.split(','))
    } else {
        return new Set()
    }
}

export const getRedactedNumber = (caller_details, receiver_details, isMyContact) => {
    let config = caller_details["config"]["view_me"]

    let notMyContacts = config["not_my_contacts"]
    let business = config["business_type"]
    let foreign_num = config["foreign_num"]

    var result = caller_details["phonenumber"]
    var maxRedaction = "PLAIN_TEXT"
    var isReceiverBusiness = receiver_details["is_business"]
    var isReceiverForeign = caller_details["country"] === receiver_details["country"]

    if(!isMyContact) {
         [result, maxRedaction] = redact(result, notMyContacts, maxRedaction)
    }
    if(isReceiverBusiness) {
        [result, maxRedaction] = redact(result, business, maxRedaction)
    }
    if(isReceiverForeign) {
        [result, maxRedaction] = redact(result, foreign_num, maxRedaction)
    }

    return result
}

const redact = (number, redactionType, maxRedaction) => {
    if((redactionType == "PLAIN_TEXT" ) || (maxRedaction == redactionType) ||
    (maxRedaction == "REDACTED" && redactionType == "MASKED") ||
    (maxRedaction == "MASKED" && redactionType == "PLAIN_TEXT")) {
        return [number, maxRedaction]
    }

   if(redactionType == "MASKED") {
       return [maskNumber(number), redactionType]
   }
   return [redactNumber(number), redactionType]
}

const maskNumber = (phoneNumber) => {
    var result = phoneNumber[0]+phoneNumber[1]
    for(let i=2; i < phoneNumber.length-2; i++) {
        result += "*"
    }
    result += phoneNumber[phoneNumber.length-2] + phoneNumber[phoneNumber.length-1]

    return result
}

const redactNumber = (phoneNumber) => phoneNumber.replace(/[0-9]/g, "*")
const headers = { authorization: token };
export const getSkyflowIdByPhoneNumber = (phoneNumber)=>{

    const query = `select redaction(skyflow_id,'PLAIN_TEXT') from table1 where phonenumber = ${phoneNumber};`

    return axios.post(
        'https://sb.area51.vault.skyflowapis.dev/v1/vaults/e728297fbdf846cfacff7fa13adb8b15/query',
        {
         query:query
        },
        {
          headers,
        },
      ).then((res)=>{
          return res.data
      }).catch((err)=>{
          console.log('Error',err)
      })

      

}