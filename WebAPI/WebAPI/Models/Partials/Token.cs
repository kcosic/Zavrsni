using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Token
    {
        public static TokenDTO ToDTO(Token item,int level)
        {
            if (item == null || level <= 0)
            {
                return null;
            }

            return new TokenDTO
            {
                TokenValue = item.TokenValue,
                User = User.ToDTO(item.User, level - 1),
                UserId = item.UserId,
                Shop = Shop.ToDTO(item.Shop, level - 1),
                ShopId = item.ShopId
            };
        }

        public static ICollection<TokenDTO> ToListDTO(ICollection<Token> list,int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public TokenDTO ToDTO(int level)
        {
            return level > 0 ? new TokenDTO
            {
                TokenValue = TokenValue,
                User = User.ToDTO(User, level - 1),
                UserId = UserId,
                Shop = Shop.ToDTO(Shop, level - 1),
                ShopId = ShopId
            } : null;
        }
    }
}