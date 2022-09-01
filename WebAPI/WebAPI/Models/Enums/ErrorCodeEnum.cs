using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models.Enums
{
    public enum ErrorCodeEnum
    {
        UnexpectedError,
        Forbidden,
        InvalidToken,
        UsernameExists,
        EmailExists,
        InvalidParameter,
        LegalNameExists,
        ShortNameExists,
        VatExists
    }
}