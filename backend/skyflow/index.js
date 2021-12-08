const getRowFromVault = (device_id) => {
    // Todo: get row from vault based on tokens stored in local DB

    /*
        Input: {
            device_id: ID of user, primary_key in local DB
        }
    */

    /*
    steps:
    1. get row of tokens from local DB with primary_key "device_id" (Use: DBClient.get('device_id'))
    2. get tokens from vault using detokenize API
    */
}

const updateContacts = (device_id, contacts) => {
    // Todo: update user data with contacts
    
    /*
        input: {
            device_id: user device ID, primary key in local DB
            contacts: Array of phone numbers
        }
    */

    /*
        steps:
        1. Get required tokens from local DB (TODO)
        2. Call the update API with necessary details (TODO)
        3. Store the tokens returned from API call in local DB (TODO)
    */
}

export const skyflowClient = {
    get: getRowFromVault,
    updateContacts: updateContacts,
}