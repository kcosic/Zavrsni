using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.Bases;
using WebAPI.Models.Enums;

namespace WebAPI.Models.Exceptions
{
    public class ForbiddenExepction : BaseException
    {
        public ForbiddenExepction() : base("Forbidden")
        {
            _errorCode = ErrorCodeEnum.Forbidden;
        }
    }
}