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
        querySelectWhere
        ('ORDER BY')?
        ('LIMIT')?
        {$result = new KraQLQuerySelect($tableName.text, $querySelectFields.result, $querySelectWhere.result);}
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

querySelectWhere returns [HashMap<String, String> result]
    :   {$result = new HashMap();}
        (
            'WHERE'
            field1 = string EQUALS value1 = stringQuoteSingle
            {$result.put($field1.text, $value1.result);}
            (
                COMMA field2 = string EQUALS value2 = stringQuoteSingle
                {$result.put($field2.text, $value2.result);}
            )*
        )?
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