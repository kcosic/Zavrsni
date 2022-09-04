using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Token
    {
        public static TokenDTO ToDTO(Token item, bool singleLevel = true)
        {
            if (item == null)
            {
                return null;
            }

            return new TokenDTO
            {
                TokenValue = item.TokenValue,
                User = singleLevel ? null : User.ToDTO(item.User),
                UserId = item.UserId
            };
        }

        public static ICollection<TokenDTO> ToListDTO(ICollection<Token> list, bool singleLevel = true)
        {
            if (list == null)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public TokenDTO ToDTO(bool singleLevel = true)
        {
            return new TokenDTO
            {
                TokenValue = TokenValue,
                User = singleLevel ? null : User.ToDTO(User),
                UserId = UserId
            };
        }
    }
}