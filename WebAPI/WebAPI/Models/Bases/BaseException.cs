using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.Enums;

namespace WebAPI.Models.Bases
{
    public abstract class BaseException : Exception
    {
        protected ErrorCodeEnum _errorCode;
        public ErrorCodeEnum ErrorCode
        {
            get
            {
                return _errorCode;
            }
        }

        public BaseException(string message) : base(message)
        {

        }
    }
}