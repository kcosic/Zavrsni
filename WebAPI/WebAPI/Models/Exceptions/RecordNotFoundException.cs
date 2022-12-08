using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models.Exceptions
{

    public class RecordNotFoundException : Exception
    {
        /// <summary>
        /// Exception for when record was not found in the database, either missing or deleted
        /// </summary>
        public RecordNotFoundException() : base("Record not found")
        {

        }        
        public RecordNotFoundException(string message) : base(message)
        {

        }
    }
}