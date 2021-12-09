
export const shouldBlock= (receiver_rules, caller_details) => {
    const blockedCountries = getCountrySet(receiver_rules["blocked_countries"])
    const blockBusiness = receiver_rules["block_business"]
    const blockSpam = receiver_rules["block_spam"]
    
    const callerCountry = caller_details["country"]
    const isCallerBusiness = caller_details["is_business"]
    const isCallerSpam = caller_details["is_spam"]


    return (callerCountry in blockedCountries || (blockBusiness && isCallerBusiness) && (blockSpam && isCallerSpam))
}

const getCountrySet = (countriesStr) =>  new Set(countriesStr.split(','))

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