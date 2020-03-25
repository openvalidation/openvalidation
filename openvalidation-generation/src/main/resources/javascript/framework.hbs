var HUMLFramework = function() {
    this.rules = [];

    this.appendRule = function(name, fields, error, condition, disabled) {
            this.rules.push( {
                name : name,
                method : condition,
                error: error,
                fields:fields,
                disabled : disabled
        });
    }

    this.createVariable = function(name, clbk) {
        return {
            name : name,
            clbk : clbk,
            GetValue : function(model) {
                return this.clbk(model)
            }
        };
    }

    this.validate = function(model) {
        var errorSummary = {
            hasErrors:false,
            errors:[],
            fields:[]
        };

        var cntx = this;

        for(var x=0; x < this.rules.length; x++) {
            if(!this.rules[x].disabled){

                if (this.rules[x].method(model, cntx)) {
                    errorSummary.errors.push({
                        error: this.rules[x].error,
                        fields: this.rules[x].fields,
                    });

                    errorSummary.hasErrors = true;

                    if (this.rules[x].fields) {
                        this.rules[x].fields.forEach(f => {
                            if(!errorSummary.fields[f])
                                errorSummary.fields.push(f);
                    });
                    }

                }
            }
        }

        return errorSummary;
    }

    this.getEnabledRules = function() {
        var arRules = [];

        for(var x=0; x < this.rules.length; x++) {
            if (!this.rules[x].disabled && this.rules[x].error)
                arRules.push(this.rules[x]);
        }

        return arRules;
    }

    this.WHERE = function(arr, clbk) {
        var out = [];

        for(var x=0; x < arr; x++){
            if (clbk(arr[x])){
                out.push(this[x]);
            }
        }

        return (out.length() == 1)? out[0] : out;
    }

    this.ONE_OF = function(value, ...params){
        if(params.length == 1 && params[0].length > 0)
            return params[0].includes(value);
        return params.includes(value);
    }

    this.EQUALS = function(leftOperand, rightOperand)
    {
        return leftOperand == rightOperand;
    }

    this.NOT_EQUALS = function(leftOperand, rightOperand)
    {
        return !this.EQUALS(leftOperand, rightOperand);
    }

    this.LESS_THAN = function(leftOperand, rightOperand)
    {
        return leftOperand < rightOperand;
    }

    this.GREATER_THAN = function(leftOperand, rightOperand)
    {
        return leftOperand > rightOperand;
    }

    this.LESS_OR_EQUALS = function(leftOperand, rightOperand)
    {
        return leftOperand <= rightOperand;
    }

    this.GREATER_OR_EQUALS = function(leftOperand, rightOperand)
    {
        return leftOperand >= rightOperand;
    }

    this.AT_LEAST_ONE_OF = function(leftOperand, rightOperand)
    {
        return (rightOperand && rightOperand.length > 0)?
                    rightOperand.includes(leftOperand) : false;
    }

    this.NONE_OF = function(leftOperand, rightOperand)
    {
        return !this.AT_LEAST_ONE_OF(leftOperand, rightOperand);
    }

    this.SUM_OF = function(operand){
        var value = 0;
        for (let i=0; i<operand.length; i++) {
            value += operand[i];
          }
        return value;
    }

    this.EMPTY = function(operand) {
        if (!operand) return true;

        if (Array.isArray(operand))
            return operand.length < 1;

        return false;
    }

    this.NOT_EMPTY = function(operand) {
        return !this.EMPTY(operand);
    }

    this.CREATE_ARRAY = function(...x) {
        return x;
    }
}
