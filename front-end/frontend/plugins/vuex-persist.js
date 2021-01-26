import VuexPersistance from 'vuex-persist'

import SecureLS from 'secure-ls'
var secureLs = new SecureLS()

export default({store}) => {
    new VuexPersistance({
        storage: secureLs,
        restoreState: (key, storage) => storage.get(key),
        saveState: (key, state, storage) => storage.set(key, state)
    }).plugin(store)
}
