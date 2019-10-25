grammar KraQLQuery;

@header
{
    import craicoverflow89.kraql.components.*;
    import java.lang.StringBuffer;
    import java.util.HashMap;
}

// Parser Rules
query returns [KraQLQuery result]
    :   (
            queryInsert {$result = $queryInsert.result;}
        |
            querySelect {$result = $querySelect.result;}
        ) EOF
    ;

queryInsert returns [KraQLQueryInsert result]
    :   'INSERT INTO' tableName = string PAREN1 queryInsertFields PAREN2
        'VALUES' PAREN1 queryInsertRecords PAREN2
        {$result = new KraQLQueryInsert($tableName.text, $queryInsertFields.result, $queryInsertRecords.result);}
    ;

queryInsertFields returns [ArrayList<String> result]
    @init {ArrayList<String> fields = new ArrayList();}
    :   field1 = string {fields.add($field1.text);}
        (
            COMMA field2 = string {fields.add($field2.text);}
        )*
        {$result = fields;}
    ;

queryInsertRecords returns [ArrayList<String> result]
    @init {ArrayList<String> fields = new ArrayList();}
    :   field1 = string {fields.add($field1.text);}
        (
            COMMA field2 = string {fields.add($field2.text);}
        )*
        {$result = fields;}
    ;

querySelect returns [KraQLQuerySelect result]
    :   'SELECT' querySelectFields
        'FROM' tableName = string
        clauseWhere
        ('ORDER BY')?
        ('LIMIT')?
        {$result = new KraQLQuerySelect($tableName.text, $querySelectFields.result, $clauseWhere.result);}
    ;

querySelectFields returns [ArrayList<String> result]
    @init {ArrayList<String> fields = new ArrayList();}
    :   (
            field1 = string {fields.add($field1.text);}
            (
                COMMA field2 = string {fields.add($field2.text);}
            )*
        |
            '*'
        )
        {$result = fields;}
    ;

clauseWhere returns [ArrayList<KraQLQueryCondition> result]
    @init {ArrayList<KraQLQueryCondition> conditions = new ArrayList();}
    :   (
            'WHERE'
            clause1 = clauseWhereAny
            {conditions.add($clause1.result);}
            (
                COMMA clause2 = clauseWhereAny
                {conditions.add($clause2.result);}
            )*
        )?
        {$result = conditions;}
    ;

clauseWhereAny returns [KraQLQueryCondition result]
    :   (
            clauseWhereEquals {$result = $clauseWhereEquals.result;}
        |
            clauseWhereLike {$result = $clauseWhereLike.result;}
        )
    ;

clauseWhereEquals returns [KraQLQueryConditionEquals result]
    :   field = string EQUALS value = stringQuoteSingle
        {$result = new KraQLQueryConditionEquals($field.text, $value.result);}
    ;

clauseWhereLike returns [KraQLQueryConditionLike result]
    :   field = string 'LIKE' value = stringQuoteSingle
        {$result = new KraQLQueryConditionLike($field.text, $value.result);}
    ;

stringQuoteSingle returns [String result]
    :   {StringBuffer buffer = new StringBuffer();}
        QUOTE_SINGLE
        string1 = string {buffer.append($string1.text);}
        (
            SPACE string2 = string
            {buffer.append(" " + $string2.text);}
        )*
        QUOTE_SINGLE
        {$result = buffer.toString();}
    ;

string
    :   CHAR+
    ;

// Lexer Rules
COMMA: ',';
PAREN1: '(';
PAREN2: ')';
EQUALS: '=';
WHITESPACE: [ \t\r\n]+ -> skip;
SPACE: ' ';
QUOTE_SINGLE: '\'';
CHAR: ~['];