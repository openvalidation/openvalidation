import math

class HUMLFramework:
    def __init__(self):
        self.rules = list()

    def append_rule(self, name, fields, error, condition_function, disabled):
        self.rules.append(ValidationRule(name, fields, error, condition_function, disabled))

    def validate(self, model):
        has_errors = False
        errors = list()
        fields = list()

        for rule in self.rules:
            if not rule.disabled and rule.method(model, self):
                errors.append({
                    "error": rule.error,
                    "fields": rule.fields
                })

                has_errors = True
                fields += [field for field in rule.fields if field not in fields]

        return {
            "has_errors": has_errors,
            "errors": errors,
            "fields": fields
        }

    def get_enabled_rules(self):
        return [rule for rule in self.rules if not rule.disabled and rule.error]

class ValidationRule:
    def __init__(self, name, fields, error, condition_function, disabled):
        self.name = name
        self._condition_function = condition_function
        self.error = error
        self.fields = fields
        self.disabled = disabled

    def method(self, model, context):
        return self._condition_function(model, context)

class Variable:
    def __init__(self, name, value_function):
        self.name = name
        self._value_function = value_function

    def get_value(self, model):
        return self._value_function(model)

def EQUALS(left_operand, right_operand):
    try:
        if isinstance(left_operand, str) and not isinstance(right_operand, str):
            return type(right_operand)(left_operand) == right_operand
        elif not isinstance(left_operand, str) and isinstance(right_operand, str):
            return left_operand == type(left_operand)(right_operand)
        return left_operand == right_operand
    except (ValueError, TypeError):
        return False

def NOT_EQUALS(left_operand, right_operand):
    return not EQUALS(left_operand, right_operand)

def LESS_THAN(left_operand, right_operand):
    try:
        if isinstance(left_operand, str) and not isinstance(right_operand, str):
            return type(right_operand)(left_operand) < right_operand
        elif not isinstance(left_operand, str) and isinstance(right_operand, str):
            return left_operand < type(left_operand)(right_operand)
        elif right_operand is not None and left_operand is None:
            return True
        return left_operand < right_operand
    except (ValueError, TypeError):
        return False

def GREATER_THAN(left_operand, right_operand):
    try:
        if isinstance(left_operand, str) and not isinstance(right_operand, str):
            return type(right_operand)(left_operand) > right_operand
        elif not isinstance(left_operand, str) and isinstance(right_operand, str):
            return left_operand > type(left_operand)(right_operand)
        elif right_operand is None and not left_operand is None:
            return True
        return left_operand > right_operand
    except (ValueError, TypeError):
        return False

def LESS_OR_EQUALS(left_operand, right_operand):
    return LESS_THAN(left_operand, right_operand) or EQUALS(left_operand, right_operand)

def GREATER_OR_EQUALS(left_operand, right_operand):
    return GREATER_THAN(left_operand, right_operand) or EQUALS(left_operand, right_operand)

def WHERE(array, condition_function):
    return [el for el in array if condition_function(el)]

def ONE_OF(left_operand, right_operand):
    try:
        return sum(map(lambda el: left_operand == el), right_operand) == 1
    except TypeError:
        return False

def AT_LEAST_ONE_OF(left_operand, right_operand):
    try:
        return left_operand in right_operand
    except TypeError:
        return False

def NONE_OF(left_operand, right_operand):
    return not AT_LEAST_ONE_OF(left_operand, right_operand)

def EMPTY(operand):
    try:
        return math.isnan(operand)
    except TypeError:
        return not operand

def NOT_EMPTY(operand):
    return not EMPTY(operand)

def EXISTS(operand):
    return operand is not None

def NOT_EXISTS(operand):
    return not EXISTS(operand)

def SUM_OF(operand):
    try:
        return sum(operand)
    except TypeError:
        return 0.0

def FIRST(operand, property_selector = id, amount = 1):
    try:
        return map(property_selector, operand)[0:amount]
    except TypeError:
        return operand

def LAST(operand, property_selector = id, amount = 1):
    try:
        return map(property_selector, operand)[-amount:len(operand)]
    except TypeError:
        return operand

def GET_ARRAY_OF(operand, property_selector):
    try:
        return list(map(property_selector, operand))
    except TypeError:
        return operand
