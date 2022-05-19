using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.Enums;

namespace WebAPI.Models.Responses
{
    public class ErrorResponse : BaseResponse
    {
        public ErrorCodeEnum ErrorCode { get; set; }
    }
}