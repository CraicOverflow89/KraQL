grammar KraQLQuery;

@header
{
    import craicoverflow89.kraql.components.*;
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
        ('WHERE')?
        ('ORDER BY')?
        ('LIMIT')?
        {$result = new KraQLQuerySelect($tableName.text, $querySelectFields.result);}
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

string
    :   CHAR+
    ;

// Lexer Rules
COMMA: ',';
PAREN1: '(';
PAREN2: ')';
WHITESPACE: [ \t\r\n]+ -> skip;
CHAR: .;