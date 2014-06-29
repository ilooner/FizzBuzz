var DELAY_MIN = 0;
var DELAY_MAX = 60 * 24;

var PHONE_NUMBER_REGEX = /^\d{3}-\d{3}-\d{4}|\d{10}$/;

$(document).ready(function()
{
    $("#progressDialog").hide();
    $("#requestRecieved").hide();
    
    $("#phoneNumberErrorBox").hide();
    $("#delayErrorBox").hide();
    
    $.ajax({
    url: "https://www.tricktry.com/FizzBuzz-war/webresources/fizzbuzz/calls",
    success: getCalls,
    contentType: 'application/json',
    type: 'GET'
    });
});

function submitFunction()
{
    fixFields();
    
    var validated = validateFields();
    
    if(!validated)
    {
        return;
    }
    
    $("#progressDialog").dialog
    ({
        modal: true
    });
    
    $("#progressbar").progressbar
    ({
        value: false
    });
    
    callRequest = "{\"PhoneNumber\": " + $('#phoneNumberField').val() + ", \"MinuteDelay\": " + $('#delayField').val() + "}";
    
    $.ajax({
    url: "https://www.tricktry.com/FizzBuzz-war/webresources/fizzbuzz",
    success: callSuccess,
    error: callError,
    complete: callComplete,
    contentType: 'application/json',
    type: 'PUT',
    data: callRequest
    });
}

function fixFields()
{
    //Fix phone number
    
    var phoneNumber = $("#phoneNumberField").val();
    phoneNumber = removeWhiteSpace(phoneNumber);
    phoneNumber = phoneNumber.replace(/-+/g, '-');
    $("#phoneNumberField").val(phoneNumber);
    
    //Fix delay field
    
    $("#delayField").val(removeWhiteSpace($("#delayField").val()));
}

function validateFields()
{
    var phoneNumberValidateResult = validatePhoneNumber();
    var valid = true;
    
    if(phoneNumberValidateResult !== null)
    {
        valid = false;
        $("#phoneNumberErrorString").text(phoneNumberValidateResult);
        $("#phoneNumberErrorBox").show();
    }
    else
    {
        $("#phoneNumberErrorBox").hide();
    }
    
    var delayValidateResult = validateDelay();
    
    if(delayValidateResult !== null)
    {
        valid = false;
        $("#delayErrorString").text(delayValidateResult);
        $("#delayErrorBox").show();
    }
    else
    {
        $("#delayErrorBox").hide();
    }
    
    return valid;
}

function validatePhoneNumber()
{
    var phoneNumber = $("#phoneNumberField").val();
    
    if(phoneNumber === '')
    {
        return "No phone number was entered.";
    }
    
    if(!PHONE_NUMBER_REGEX.test(phoneNumber))
    {
        return "This not a valid phone number.";
    }
    
    return null;
}

function validateDelay()
{
    var delay = $("#delayField").val();
    
    if(delay === '')
    {
        return "No delay was entered.";
    }
    
    delay = parseInt(delay);
    
    if(isNaN(delay))
    {
        return "Is not a valid number.";
    }
    
    if(delay < DELAY_MIN)
    {
        return "The delay cannot be less than " + DELAY_MIN + ".";
    }
    
    if(delay > DELAY_MAX)
    {
        return "The delay cannot be greater than " + DELAY_MAX + ".";
    }
    
    return null;
}

function removeWhiteSpace(valueString)
{
    return valueString.replace(/\s+/g, '');
}

function callComplete()
{
    $("#progressDialog").dialog("close");
    
    $("#requestRecieved").dialog
    ({
        modal: true,
        buttons:
        {
            Ok: function() 
            {
                $(this).dialog("close");
            }
        }
    });
}

function callSuccess()
{
    $("#callStatusMessage").text("Information Recieved! You'll hear from us soon!");
}

function callError()
{
    $("#callStatusMessage").text("We did not recieve your information. Please try again.");
}

/*
 *     private String phoneNumberString;
    
    @Column(name="minute_delay",
            nullable=false)
    @XmlElement(name="minuteDelayInt")
    private Integer minuteDelayInt;
    
    @Column(name="fizz_buzz_number",
            nullable=false)
    @XmlElement(name="fizzBuzzNumberInt")
    private Integer fizzBuzzNumberInt;
    
    @Column(name="date_value",
            nullable=false)
    private Long dateValue;
    
    @XmlElement(name="dateString")
    @Transient
    private String dateString;
    
 */

function getCalls(data)
{
    console.log(data);
    $("#tableHolder").append("<table border=\"1\"></table>");
    
    var table = $("#tableHolder > table");
    table.append("<tr><th>Phone Number</th><th>Minute Delay</th><th>Fizz Buzz Number</th><th>Date</th><th>Replay</th></tr>")
    
    for(var callCounter = 0;
        callCounter < data.fizzBuzzCalls.length;
        callCounter++)
    {
        var call = data.fizzBuzzCalls[callCounter];
        console.log(call);
        
        var row = "<tr>" +
                  "<td>" +
                  call.phoneNumberString +
                  "</td>" +
                  "<td>" +
                  call.minuteDelayInt +
                  "</td>" +
                  "<td>" +
                  call.fizzBuzzNumberInt +
                  "</td>" +
                  "<td>" +
                  call.dateString +
                  "</td>" +
                  "<td><input type=\"button\" value=\"re\" onclick=\"replay('" + call.phoneNumberString + "','" + call.fizzBuzzNumberInt + "')\"/></td>" +
                  "</tr>";
        table.append(row);
    }
}

function replay(phoneNumber, fizzBuzzNumber)
{
    $("#progressDialog").dialog
    ({
        modal: true
    });
    
    $("#progressbar").progressbar
    ({
        value: false
    });
    
    var callRequest = "{\"phoneNumber\": " + phoneNumber + ", \"fizzBuzzNumber\": " + fizzBuzzNumber + "}";
    
        $.ajax({
    url: "https://www.tricktry.com/FizzBuzz-war/webresources/fizzbuzz/replay",
    success: callSuccess,
    error: callError,
    complete: callComplete,
    contentType: 'application/json',
    type: 'PUT',
    data: callRequest
    });
}