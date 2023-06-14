import Keycloak from 'keycloak-js'

const keycloak = Keycloak({
    url: 'http://localhost:8081/realms/auto-realm/protocol/openid-connect/auth',
    realm: 'auto-realm',
    clientId: 'test-client'
})

export {keycloak}