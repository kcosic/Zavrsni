using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.Bases;
using WebAPI.Models.Enums;

namespace WebAPI.Models.Exceptions
{
    public class InvalidCredentialsException : BaseException
    {
        public InvalidCredentialsException() : base ("Invalid login.")
        {
            _errorCode = ErrorCodeEnum.InvalidCredentials;
        }
    }
}