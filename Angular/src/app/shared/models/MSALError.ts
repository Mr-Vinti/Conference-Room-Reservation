export class MSALError {
    private _error;
    private _errorDesc;
    private _scopes;
    constructor(error: string, errorDesc?: string, scopes?: string){
        this._error = "";
        this._errorDesc = "";
        this._scopes = "";
        this._error = error;
        if (errorDesc) {
            this._errorDesc = errorDesc;
        }
        if (scopes) {
            this._scopes = scopes;
        }
    }
    error: string;
    errorDesc: string;
    scopes: string;
}
